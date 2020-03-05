package cn.tycad.oa.exam.excel;

import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.exception.BusinessException;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: YY
 * @Date: 2019/3/18
 * @Description: Excel对应的考试对象
 */
@Data
public class ExcelExamInfo extends BaseRowModel{
    @ExcelProperty(value = {"考试名称"},index = 0)
    private String examName;
    @ExcelProperty(value = {"考试标题"},index = 1)
    private String subTitle;
    @ExcelProperty(value = {"考试描述"},index = 2)
    private String description;
    /**
     * 表示仅仅内部考试（系统用户考试），培训考试（不仅系统用户，而且可以支持一次性用户扫码录入考试）
     */
    @ExcelProperty(index = 3 , value = {"考试类型"})
    private String examTypeString;

    private Integer examType;
    @ExcelProperty(index = 4 , value = {"发布时间"})
    private String publishTime;
    @ExcelProperty(index = 5 , value = {"截至时间"})
    private String deadLine;
    @ExcelProperty(value = {"考试时长"},index = 6)
    private String duration;
    @ExcelProperty(value = {"阅卷老师"},index = 7)
    private String teacher;
    @ExcelProperty(value = {"指定考生"},index = 8)
    private String students;

    /**
     * 将Excel中试题类型的选中的下拉文字设置为整形数字描述
     */
    public void changeExamType() {
        if (StringUtils.isBlank(this.examTypeString)){
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_EXAMTYPE_NOT_VALID);
        }
        switch (this.examTypeString.trim()){
            case "内部考试":{
                this.examType = 0;
                break;
            }
            case "培训考试":{
                this.examType = 1;
                break;
            }
            default: {
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_EXAMTYPE_NOT_VALID);
            }
        }
    }
}
