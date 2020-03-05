package cn.tycad.oa.exam.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: YY
 * @Date: 2019/8/2
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAllOptionAvgVo {
    @ApiModelProperty(value = "讲师课程内容准备之充分度", required = true)
    private Double wellPreparedAvg;

    @ApiModelProperty(value = "讲师授课的音量、口齿清晰度", required = true)
    private Double pronunciationAvg;

    @ApiModelProperty(value = "讲师专业知识/实践经验", required = true)
    private Double experienceAvg;

    @ApiModelProperty(value = "讲师授课思路及条理性", required = true)
    private Double logicAvg;

    @ApiModelProperty(value = "课程内容理解程度", required = true)
    private Double understandableAvg;

    @ApiModelProperty(value = "教材制作精美度", required = true)
    private Double handoutQualityAvg;

    @ApiModelProperty(value = "讲师与学员课堂内互动状况", required = true)
    private Double interactionAvg;

    @ApiModelProperty(value = "讲师对学员提出的问题有无合理及满意的解答", required = true)
    private Double answerOnQuestionAvg;

    @ApiModelProperty(value = "课程对你本身工作之帮助性", required = true)
    private Double helpfulAvg;

    @ApiModelProperty(value = "课程安排之合理性、设施配备之良好性", required = true)
    private Double sensibleAvg;

    @ApiModelProperty(value = "老师姓名", required = true)
    private String teacherUserName;
}
