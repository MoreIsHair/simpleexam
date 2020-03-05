package cn.tycad.oa.exam.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/4/3
 * @Description: 用来统计所有用户的所有信息（考试信息、用户信息、部门、分数）
 */
@Data
@NoArgsConstructor
@ApiModel(description = "用来统计所有用户的所有信息（考试信息、用户信息、部门、分数）")
public class TbExamAndUserInfoAndDepartInfoBo {
    @ApiModelProperty(value = "考试id")
    private String examId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "部门id")
    private String departmentId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "部门名")
    private String departmentName;

    @ApiModelProperty(value = "公司名")
    private String companyName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "考试完成时间")
    private Date finishExamTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "考试开始时间")
    private Date startExamTime;

    @ApiModelProperty(value = "分数")
    private Float score;

}
