package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbExam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/3/11
 * @Description: 考试信息与分数
 */
@Data
@ApiModel(description = "考试信息与分数")
public class SingleExamAndScoreBo extends TbExam {
    @ApiModelProperty(value = "考试分数")
    private Float score;
    @ApiModelProperty(value = "是否已评价")
    private boolean reviewed;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "暂停考试时间")
    private Date suspendTime;
    @ApiModelProperty(value = "剩余考试时长")
    private Double surplus;
}
