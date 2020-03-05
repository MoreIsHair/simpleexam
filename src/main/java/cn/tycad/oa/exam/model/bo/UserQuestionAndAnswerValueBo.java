package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.excel.ExcelQuestionInfo;
import cn.tycad.oa.exam.model.entity.TbQuestion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/3/11
 * @Description: 非选择题以及用户的回答值
 */
@Data
@AllArgsConstructor
@ApiModel(description = "非选择题以及用户的回答值")
public class UserQuestionAndAnswerValueBo extends TbQuestion {
    @ApiModelProperty(value = "回答内容")
    private String answerValue;

    @ApiModelProperty(value = "分数")
    private Float userScore;

    @ApiModelProperty("参考答案")
    private String validValue;

    public UserQuestionAndAnswerValueBo() {
    }

    public UserQuestionAndAnswerValueBo(String questionId, String questionTitle, String questionText, String examId, Float questionScore, Integer orderNum, Integer questionType, String answerValue, Float userScore) {
        super(questionId, questionTitle, questionText, examId, questionScore, orderNum, questionType);
        this.answerValue = answerValue;
        this.userScore = userScore;
    }

    public UserQuestionAndAnswerValueBo(ExcelQuestionInfo excelQuestionInfo) {
        super(excelQuestionInfo);
    }
}
