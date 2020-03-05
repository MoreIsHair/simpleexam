package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbUserExam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/4/4
 * @Description: 单次考试的所有用户信息（分数、考试完成时间、考试信息）
 */
@Data
@ApiModel(description = "单次考试的所有用户信息（分数、考试完成时间、考试信息）")
public class SingleExamAndScoreAndUserInfo extends TbUserExam {
    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "考试所属部门id")
    private String departmentId;

    @ApiModelProperty(value = "考试所属部门名")
    private String departmentName;

    @ApiModelProperty(value = "阅卷老师名称")
    private String teacherUserName;
}
