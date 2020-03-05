package cn.tycad.oa.exam.excel;

import cn.tycad.oa.exam.model.bo.TbExamAndAverageBo;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/4/22
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExamGradeInfo extends BaseRowModel {
    private String examId;
    @ExcelProperty(value = {"试题名称"},index = 0)
    private String examName;
    @ExcelProperty(value = {"考试时间（发布时间）"},index = 1)
    private Date publishTime;
    @ExcelProperty(value = {"考试时长"},index = 2)
    private Integer duration;
    @ExcelProperty(value = {"平均分"},index = 3)
    private Float average;

    public ExcelExamGradeInfo(TbExamAndAverageBo avgBos) {
        this.average = avgBos.getAverage();
        this.duration = avgBos.getDuration();
        this.examId = avgBos.getExamId();
        this.publishTime = avgBos.getPublishTime();
        this.examName = avgBos.getExamName();


    }
}
