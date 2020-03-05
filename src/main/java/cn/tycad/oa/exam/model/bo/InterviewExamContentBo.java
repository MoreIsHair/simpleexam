package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbInterviewQuestion;
import cn.tycad.oa.exam.model.entity.TbInterviewTemplate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/8/12
 * @Description:
 */
@Data
public class InterviewExamContentBo {
    @ApiModelProperty("考试模板信息")
    private TbInterviewTemplate exam;
    /**
     * 选择题及其选项
     */
    @ApiModelProperty(value = "选择题及其选项")
    private List<InterviewQuestionAndOptionBo> optionQuestionAndOptionBos;
    /**
     * 非选择题(判断题)
     */
    @ApiModelProperty(value = "非选择题(判断题)")
    private List<TbInterviewQuestion> questions;
}
