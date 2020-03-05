package cn.tycad.oa.exam.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: shizf
 * @Date: 0415
 * @Description: 用户组模型
 */
@Data
@ApiModel(value = "SystemGroup", description = "用户组模型")
public class SystemGroup {
    @ApiModelProperty(value = "分组id")
    private String groupId;
    @ApiModelProperty(value = "分组名")
    private String groupName;
    @ApiModelProperty(value = "所属部门id")
    private String departmentId;
}
