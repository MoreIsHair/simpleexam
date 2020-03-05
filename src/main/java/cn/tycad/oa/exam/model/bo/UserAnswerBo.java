package cn.tycad.oa.exam.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: shizf
 * @Date: 190319
 * @Description: 用户答卷BO
 */
@Data
@ApiModel(description = "用户回答模型")
public class UserAnswerBo {
    @NotNull
    @ApiModelProperty(value = "考试id")
    private String examId;

    @NotNull
    @ApiModelProperty(value = "用户id,或者扫描填写的用户名",required = true)
    private String userId;

    @NotNull
    @ApiModelProperty(value = "参考用户类型，内部员工0，培训考试扫描用户（表示允许内部人员以及扫描录入基本信息用户）1",required = true)
    private Integer userType;

    @ApiModelProperty(value = "考试剩余时长")
    private Long commitTime;

    @ApiModelProperty(value = "用户回答列表")
    @NotNull
    List<UserOptionBo> answers;
}
