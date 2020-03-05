package cn.tycad.oa.exam.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @Author: shizf
 * @Date: 0423
 * @Description: 公共工具类
 */
public class CommonUtils {

    /**
     * 将array转成list
     * @param array
     * @param <T>
     * @return
     */
    public static <T> List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList<>();
        for (T t: array
             ) {
            list.add(t);
        }

        return list;
    }

    /**
     * 对时间进行加减
     * @param originalTime
     * @param typeAndNumber map<Calendar.YEAR，size>
     * @return
     */
    public static Date time(Date originalTime , Map<Integer,Integer> typeAndNumber){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(originalTime);
        for (int i : typeAndNumber.keySet()) {
            calendar.add(i, typeAndNumber.get(i));
        }
        return calendar.getTime();
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     * @param delPath
     * @return
     * @throws Exception
     */
    public static boolean deleteFile(String delPath) throws Exception {
        try {

            File file = new File(delPath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] fileList = file.list();
                for (int i = 0;fileList != null && i < fileList.length; i++) {
                    File delFile = new File(delPath + "\\" + fileList[i]);
                    if (!delFile.isDirectory()) {
                        delFile.delete();
                        System.out.println(delFile.getAbsolutePath() + "删除文件成功");
                    } else if (delFile.isDirectory()) {
                        deleteFile(delPath + "\\" + fileList[i]);
                    }
                }
                System.out.println(file.getAbsolutePath() + "删除成功");
                file.delete();
            }
        } catch (FileNotFoundException e) {
            throw new Exception(e.getMessage());
        }
        return true;
    }
}
