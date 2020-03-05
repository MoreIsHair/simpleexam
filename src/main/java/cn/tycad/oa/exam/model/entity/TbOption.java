package cn.tycad.oa.exam.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description: 选项模型
 */
@Data
@JsonInclude(Include.NON_NULL)
@ApiModel(value = "TbOption", description = "选择题，选项模型")
public class TbOption {
    @ApiModelProperty(value = "选项id")
    private String optionId;

    @ApiModelProperty(value = "选项文字")
    private String optionText;

    @ApiModelProperty(value = "所属问题id")
    private String questionId;

    @ApiModelProperty(value = "排序序号")
    private Integer orderNum;

    public TbOption() {
    }

    public TbOption(String optionId, String optionText, Integer orderNum) {
        this.optionId = optionId;
        this.optionText = optionText;
        this.orderNum = orderNum;
    }
}
