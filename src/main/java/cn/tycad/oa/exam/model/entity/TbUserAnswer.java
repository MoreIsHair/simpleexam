package cn.tycad.oa.exam.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "TbUserAnswer", description = "用户回答模型")
public class TbUserAnswer {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "选项id，只有选择题有，其他类型题目为空")
    private String optionId;

    @ApiModelProperty(value = "回答内容，判断题为“正确，错误”，简答题问内容")
    private String answerValue;

    @ApiModelProperty(value = "问题id")
    private String questionId;
    /**
     * 用户回答的得分
     */
    @ApiModelProperty(value = "用户回答的得分")
    private Float userScore;


    public TbUserAnswer(String userId, String questionId, Float userScore) {
        this.userId = userId;
        this.questionId = questionId;
        this.userScore = userScore;
    }

    public TbUserAnswer(String userId, String optionId, String answerValue, String questionId) {
        this.userId = userId;
        this.optionId = optionId;
        this.answerValue = answerValue;
        this.questionId = questionId;
    }
}
