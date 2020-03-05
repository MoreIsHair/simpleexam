package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbExam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/6
 * @Description: 考试分数、题目、用户回答
 */
@Data
@ApiModel(description = "考试分数、题目、用户回答")
public class SingleScoreAndQuestionBo {

    @ApiModelProperty(value = "考试对象")
    private TbExam exam;

    /**
     * 本次考试的分数
     */
    @ApiModelProperty(value = "本次考试的分数")
    private Float score;

    /**
     * 选择题及其选项以及用户所选择的
     */
    @ApiModelProperty(value = "选择题及其选项以及用户所选择的")
    private List<OptionQuestionAndOptionBo> options;

    /**
     * 判断题以及用户的AnswerValue
     */
    @ApiModelProperty(value = "判断题以及用户的AnswerValue")
    private List<UserQuestionAndAnswerValueBo> tFQuestions;

    /**
     * 简单题以及用户的AnswerValue
     */
    @ApiModelProperty(value = "简单题以及用户的AnswerValue")
    private List<UserQuestionAndAnswerValueBo> textQuestions;
}
