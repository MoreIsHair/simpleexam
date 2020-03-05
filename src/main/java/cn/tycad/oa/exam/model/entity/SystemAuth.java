package cn.tycad.oa.exam.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: shizf
 * @Date: 0415
 * @Description: 用户权限模型
 */
@Data
@ApiModel(value = "SystemAuth", description = "用户权限模型")
public class SystemAuth {
    @ApiModelProperty(value = "权限id")
    private String authId;
    @ApiModelProperty(value = "权限名称,如system_manager")
    private String name;
    @ApiModelProperty(value = "权限显示名称")
    private String showText;
}
