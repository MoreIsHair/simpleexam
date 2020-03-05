package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbExam;
import cn.tycad.oa.exam.model.entity.TbQuestion;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description: 考试的详情（题目、选项，没有回答的）
 */
@Data
@JsonInclude(Include.NON_NULL)
@ApiModel(description = "考试详情模型")
public class ExamDetailBo{
    /**
     * 选择题及其选项
     */
    @ApiModelProperty(value = "选择题及其选项")
    private List<OptionQuestionAndOptionBo> optionQuestionAndOptionBos;
    /**
     * 非选择题(判断题)
     */
    @ApiModelProperty(value = "非选择题(判断题)")
    private List<TbQuestion> questions;
    /**
     * 非选择题(简单题)
     */
    @ApiModelProperty(value = "非选择题(简单题)")
    private List<TbQuestion> textQuestions;
    /**
     * 考试内容
     */
    @ApiModelProperty(value = "考试内容")
    private TbExam exam;
}
