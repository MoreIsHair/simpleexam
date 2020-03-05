package cn.tycad.oa.exam.excel;

import cn.tycad.oa.exam.model.bo.TbExamAndUserInfoAndDepartInfoBo;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

@Data
public class ExcelUserExamScoreInfo extends BaseRowModel {
    //    @ExcelProperty(value = {"试题ID"},index = 0)
    //    private String examId;
    @ExcelProperty(value = {"试题名称"},index = 1)
    private String examName;
    @ExcelProperty(value = {"姓名"},index = 2)
    private String username;
    @ExcelProperty(value = {"考试日期"},index = 3)
    private Date examDate;
    @ExcelProperty(value = {"部门"},index = 4)
    private String department;
    @ExcelProperty(value = {"分数"},index = 5)
    private Float score;

    public ExcelUserExamScoreInfo(TbExamAndUserInfoAndDepartInfoBo userScoreBos) {
        //this.examId = userScoreBos.getExamId();
        this.examName = userScoreBos.getExamName();
        this.username = userScoreBos.getUserName();
        this.department = userScoreBos.getDepartmentName();
        this.score = userScoreBos.getScore();
        this.examDate = userScoreBos.getFinishExamTime();
    }
}
