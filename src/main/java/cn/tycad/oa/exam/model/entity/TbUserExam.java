package cn.tycad.oa.exam.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/3/6
 * @Description: 用户与考试表
 */
@Data
@ApiModel(value = "TbUserExam", description = "用户-考试模型")
public class TbUserExam {
    @ApiModelProperty(value = "试题id")
    private String examId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户考试状态，0未考，1已考")
    private Integer examStatus;

    @ApiModelProperty(value = "用户分数")
    private Float score;

    @ApiModelProperty(value = "结束考试时间")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date finishExamTime;

    @ApiModelProperty(value = "开始考试时间")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startExamTime;
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date markTime;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date suspendTime;
    @ApiModelProperty(value = "考试剩余时长")
    private Double surplus;

}
