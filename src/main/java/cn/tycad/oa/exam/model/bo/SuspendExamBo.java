package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbExam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/6/13
 * @Description: 暂停考试的试题以及保存的用户回答数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuspendExamBo {
    @ApiModelProperty(value = "考试对象")
    private TbExam exam;

    @ApiModelProperty(value = "暂时时间")
    private Date suspendTime;

    /**
     * 选择题及其选项以及用户所选择的
     */
    @ApiModelProperty(value = "选择题及其选项以及用户所选择的")
    private List<OptionQuestionAndOptionBo> optionQuestionAndOptionBos;

    /**
     * 判断题以及用户的AnswerValue
     */
    @ApiModelProperty(value = "判断题以及用户的AnswerValue")
    private List<UserQuestionAndAnswerValueBo> questions;

    /**
     * 简单题以及用户的AnswerValue
     */
    @ApiModelProperty(value = "简单题以及用户的AnswerValue")
    private List<UserQuestionAndAnswerValueBo> textQuestions;
}
