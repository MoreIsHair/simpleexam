package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.excel.ExcelQuestionInfo;
import cn.tycad.oa.exam.model.entity.TbOption;
import cn.tycad.oa.exam.model.entity.TbQuestion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description: 选择题及其选项
 */
@Data
@ApiModel(description = "选择题及其选项")
public class OptionQuestionAndOptionBo extends TbQuestion {

    public OptionQuestionAndOptionBo() {}

    /**
     * 所有选项
     */
    @ApiModelProperty(value = "所有选项")
    private List<TbOption> tbOptions;
    /**
     * 用户回答的选项
     */
    @ApiModelProperty(value = "用户回答的选项")
    private List<TbOption> userAnswers;

    @ApiModelProperty(value = "正确答案")
    private List<TbOption> validAnswers;

    /**
     * 得分
     */
    @ApiModelProperty(value = "用户得分")
    private Float userScore;

    public OptionQuestionAndOptionBo(ExcelQuestionInfo excelQuestionInfo) {
        super(excelQuestionInfo);
    }
}
