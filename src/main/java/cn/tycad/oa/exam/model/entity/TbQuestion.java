package cn.tycad.oa.exam.model.entity;

import cn.tycad.oa.exam.excel.ExcelQuestionInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: YY
 * @Date: 2019/3/6
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TbQuestion", description = "问题模型")
public class TbQuestion {

    @ApiModelProperty(value = "问题id")
    private String questionId;

    @ApiModelProperty(value = "问题题目")
    private String questionTitle;

    @ApiModelProperty(value = "问题文字")
    private String questionText;

    @ApiModelProperty(value = "所属考试id")
    private String examId;

    @ApiModelProperty(value = "问题分数")
    private Float questionScore;
    /**
     * 问题序号
     */
    @ApiModelProperty(value = "问题排序序号")
    private Integer orderNum;

    /**
     * 问题类型（是为选择题为0）
     */
    @ApiModelProperty(value = "问题类型，单选-1，多选0，判断题1，简答题2")
    private Integer questionType;


    public TbQuestion(ExcelQuestionInfo excelQuestionInfo) {
        this.questionTitle = excelQuestionInfo.getQuestionTitle();
        this.questionText = excelQuestionInfo.getQuestionText();
        this.questionType = excelQuestionInfo.getQuestionType();
        this.questionScore = excelQuestionInfo.getQuestionScore();
    }
}
