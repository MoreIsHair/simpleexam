package cn.tycad.oa.exam.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/3/5
 * @Description:
 */
@Data
@ApiModel(value = "SystemRole", description = "用户角色模型")
public class SystemRole {
    @ApiModelProperty(value = "角色id")
    private String roleId;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色说明")
    private String description;
    @ApiModelProperty(value = "角色类型")
    private Integer roleType;
}
