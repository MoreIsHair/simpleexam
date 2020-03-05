package cn.tycad.oa.exam.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: shizf
 * @Date: 190329
 * @Description: 用户答题信息
 */
@Data
@ApiModel(description = "用户答题信息")
public class UserOptionBo {
    @ApiModelProperty(value = "选项id列表，用户选择题")
    private List<String> optionIds;

    @ApiModelProperty(value = "回答内容，用于判断题和简答题")
    private String optionText;

    @ApiModelProperty(value = "题目id")
    private String questionId;

    @ApiModelProperty(value = "问题排序序号")
    private Integer orderNum;

    @ApiModelProperty(value = "问题分数")
    private Float questionScore;

    @ApiModelProperty(value = "问题类型，单选-1，多选0，判断题1，简答题2")
    private int questionType;
}
