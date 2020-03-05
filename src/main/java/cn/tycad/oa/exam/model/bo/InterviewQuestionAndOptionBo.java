package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbInterviewQuestion;
import cn.tycad.oa.exam.model.entity.TbOption;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/8/12
 * @Description:
 */
@Data
public class InterviewQuestionAndOptionBo extends TbInterviewQuestion {

    @ApiModelProperty(value = "所有选项")
    private List<TbOption> tbOptions;

    @ApiModelProperty(value = "正确答案")
    private List<TbOption> validAnswers;

}
