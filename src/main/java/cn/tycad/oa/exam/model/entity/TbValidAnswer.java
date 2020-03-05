package cn.tycad.oa.exam.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description:
 */
@Data
@ApiModel(value = "TbValidAnswer", description = "正确答案模型")
public class TbValidAnswer {
    @ApiModelProperty(value = "选项id，针对选择题")
    private String optionId;

    @ApiModelProperty(value = "答题内容，针对判断题")
    private String answerValue;

    @ApiModelProperty(value = "问题id")
    private String questionId;

    @ApiModelProperty(value = "问题分数")
    private Float questionScore;

    @ApiModelProperty(value = "问题类型，单选-1，多选0，判断题1，简答题2")
    private int questionType;
}
