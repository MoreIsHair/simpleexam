package cn.tycad.oa.exam.excel;

import cn.tycad.oa.exam.model.bo.SingleExamAndScoreAndUserInfo;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/7/23
 * @Description:
 */
@Data
@NoArgsConstructor
public class ExcelExamAndAverAgeBoInfo extends BaseRowModel {

    @ExcelProperty(value = "考试名称",index = 0)
    private String examName;

    @ExcelProperty(value = "用户名",index = 1)
    private String userName;

    @ExcelProperty(value = "考试所属部门名",index = 2)
    private String departmentName;

    @ExcelProperty(value = "用户分数",index = 3)
    private Float score;

    @ExcelProperty(value = "结束考试时间",index = 4)
    private Date finishExamTime;

    @ExcelProperty(value = "开始考试时间",index = 5)
    private Date startExamTime;

    @ExcelProperty(value = "阅卷老师名称",index = 6)
    private String teacherUserName;

    public ExcelExamAndAverAgeBoInfo(SingleExamAndScoreAndUserInfo s) {
        this.examName = s.getExamName();
        this.userName = s.getUserName();
        this.departmentName = s.getDepartmentName();
        this.score = s.getScore();
        this.finishExamTime = s.getFinishExamTime();
        this.startExamTime = s.getStartExamTime();
        this.teacherUserName = s.getTeacherUserName();
    }
}
