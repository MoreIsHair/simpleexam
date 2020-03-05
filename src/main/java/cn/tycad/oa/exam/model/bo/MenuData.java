package cn.tycad.oa.exam.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: shizf
 * @Date: 0415
 * @Description: 菜单模型
 */
@Data
@ApiModel(description = "菜单模型")
public class MenuData {
    @ApiModelProperty(value = "指代菜单")
    private String code;

    @ApiModelProperty(value = "extend")
    private String extend;

    @ApiModelProperty(value = "菜单名")
    private String name;

    @ApiModelProperty(value = "排序序号")
    private int sort;

    @ApiModelProperty(value = "路由地址")
    private String path;
}
