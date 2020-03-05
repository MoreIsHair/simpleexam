package cn.tycad.oa.exam.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: shizf
 * @Date: 0415
 * @Description: 用户菜单模型
 */
@Data
@ApiModel(value = "SystemMenu", description = "用户菜单模型")
public class SystemMenu {
    @ApiModelProperty(value = "菜单id")
    private String menuId;

    @ApiModelProperty(value = "菜单名")
    @NotNull
    private String menuName;

    @ApiModelProperty(value = "父级菜单id")
    @NotNull
    private String parentMenuId;

    @ApiModelProperty(value = "排序序号")
    private int sortOrder;

    @ApiModelProperty(value = "菜单路由")
    private String path;
}
