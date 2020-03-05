package cn.tycad.oa.exam.excel;

import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.exception.BusinessException;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @Author: YY
 * @Date: 2019/3/18
 * @Description: Excel工具类
 */
@Slf4j
public class ExcelUtil {

    @Data
    public static class ModelExcelListener extends AnalysisEventListener {

        private List<Object> data = new ArrayList<>();

        @Override
        public void invoke(Object o, AnalysisContext analysisContext) {
            try {
                if (!isAllFieldNull(o)){
                    data.add(o);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) { }

    }

    /**
     * 使用 模型 来读取Excel
     * @param inputStream Excel的输入流
     * @param clazz 考试信息模型的类
     * @param sheetNo
     * @param trim
     * @return 返回 模型 的列表(为object列表,需强转)
     */
    public static  List<Object> readExcelWithModel(InputStream inputStream, int sheetNo,
                                                  Class<? extends BaseRowModel> clazz, boolean trim
                                                  ) {
        // 解析每行结果在listener中处理
        ModelExcelListener listener = new ModelExcelListener();
        ExcelReader excelReader = new ExcelReader(inputStream,null, listener,trim);
        // 默认只有一列表头
        int size = excelReader.getSheets().size();
        if (size<sheetNo){
           log.debug("Excel缺少Sheet，可能会导致试题丢失");
           throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_SHEETS_ERROR);
        }
        excelReader.read(new Sheet(sheetNo,1,clazz));
        return  listener.getData();
    }

    public static void writeExcel(Map<String, List<? extends BaseRowModel>> sheetNameAndDateList, ExcelTypeEnum type, HttpServletResponse response) {
        if (checkParam(sheetNameAndDateList, type)) {
            return;
        }
        try {
            String fileName = new String(
                    (new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())).getBytes(), "UTF-8");
            //response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName+type.getValue());
            ExcelWriter writer2 = new ExcelWriter(response.getOutputStream(), type, true);
            setSheet(sheetNameAndDateList, writer2);
            writer2.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeSingelExcel(List<? extends BaseRowModel> sheetNameAndDateList, ExcelTypeEnum type, HttpServletResponse response,Class clazz) {
        try {
            String fileName = new String(
                    (new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())).getBytes(), "UTF-8");
            //response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName+type.getValue());
            response.setCharacterEncoding("UTF-8");
            ExcelWriter writer2 = new ExcelWriter(response.getOutputStream(), type, true);
            setSingleSheet(sheetNameAndDateList, writer2,clazz);
            writer2.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @Description setSheet数据
     */
    private static void setSheet(Map<String, List<? extends BaseRowModel>> sheetNameAndDateList, ExcelWriter writer) {
        int sheetNum = 1;
        for (Map.Entry<String, List<? extends BaseRowModel>> stringListEntry : sheetNameAndDateList.entrySet()) {
            Sheet sheet = new Sheet(sheetNum, 0, stringListEntry.getValue().get(0).getClass());
            sheet.setSheetName(stringListEntry.getKey());
            writer.write(stringListEntry.getValue(), sheet);
            sheetNum++;
        }
    }
    /**
     * @Description setSheet数据
     */
    private static void setSingleSheet(List<? extends BaseRowModel> sheetNameAndDateList, ExcelWriter writer,Class clazz) {
        if (sheetNameAndDateList == null){
            log.error("sheetNameAndDateList不能为空");
        }
            Sheet sheet = new Sheet(1, 0, clazz);
            sheet.setSheetName("sheet");
            writer.write(sheetNameAndDateList, sheet);
    }


    /**
     * @Description 校验参数
     */
    private static boolean checkParam(Map<String, List<? extends BaseRowModel>> sheetNameAndDateList, ExcelTypeEnum type) {
        if (CollectionUtils.isEmpty(sheetNameAndDateList)) {
            log.error("SheetNameAndDateList不能为空");
            return true;
        } else if (type == null) {
            log.error("导出的excel类型不能为空");
            return true;
        }
        return false;
    }

    /**
     * 获取文件拓展名
     * @param file 文件对象
     * @return 拓展名
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();

        if (fileName.lastIndexOf(".") != -1 || fileName.lastIndexOf(".") != 0) {
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (StringUtils.isBlank(fileExtension)){

                throw new BusinessException(ExceptionInfoEnum.INVALID_FILE_TYPE);
            }

            if (!fileExtension.equals("xls") && !fileExtension.equals("xlsx")){

                throw new BusinessException(ExceptionInfoEnum.INVALID_FILE_TYPE);
            }
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        return "";
    }

    /**
     * 判断该对象是否: 返回ture表示所有属性为null
     * 返回false表示不是所有属性都是null
     * @param obj
     * @return
     * @throws Exception
     */
    public static boolean isAllFieldNull(Object obj) throws Exception{
        // 得到类对象
        Class stuCla = (Class) obj.getClass();
        //得到属性集合
        Field[] fs = stuCla.getDeclaredFields();
        boolean flag = true;
        for (Field f : fs) {
            // 设置属性是可以访问的(私有的也可以)
            f.setAccessible(true);
            Object val = f.get(obj);
            //只要有1个属性不为空,那么就不是所有的属性值都为空
            if(val!=null) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isExcel2007(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 判断是否为整数
     * @param str
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 检查excel模板是否合法——检查模板表头
     * @param file
     * @param sheetIndex
     * @param clazz
     * @return
     */
    public static boolean validateTemplate(File file, String ext, int sheetIndex, Class<?> clazz) {
        Field[] fs = clazz.getDeclaredFields();
        List<String> annotations = new ArrayList<>();
        for(Field f : fs) {
            ExcelProperty ep = f.getAnnotation(ExcelProperty.class);
            annotations.add(ep.value()[0]);
        }

        Workbook wb = null;
        try {
            InputStream is = new FileInputStream(file);
            if("xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if("xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                return false;
            }
            org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(sheetIndex);
            List<String> titles = readExcelTitle(wb.getSheetAt(sheetIndex));

            // 比较titles 和 annotations
            Collections.sort(titles);
            Collections.sort(annotations);
            return titles.equals(annotations);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException", e);
            return false;
        } catch (IOException e) {
            log.error("IOException", e);
            return false;
        } catch (Exception e) {
            log.error("Exception", e);
            return false;
        }
    }

    /**
     * 读取Excel表格表头的内容
     *
     * @return String 表头内容的数组
     * @author shizf
     */
    private static List<String> readExcelTitle(org.apache.poi.ss.usermodel.Sheet sheet) throws Exception {
        org.apache.poi.ss.usermodel.Row row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        List<String> title = new ArrayList<>();
        for (int i = 0; i < colNum; i++) {
            title.add(row.getCell(i).getStringCellValue());
        }
        return title;
    }

}
