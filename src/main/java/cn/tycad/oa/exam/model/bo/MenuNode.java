package cn.tycad.oa.exam.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: shizf
 * @Date: 0415
 * @Description: 菜单节点
 */
@Data
@ApiModel(description = "菜单树节点模型")
public class MenuNode {

    @ApiModelProperty(value = "菜单id")
    private String id;

    @ApiModelProperty(value = "菜单树的层级")
    private String level;

    @ApiModelProperty(value = "父级节点id，如果是根节点，则为“-1”")
    private String parentId;

    @ApiModelProperty(value = "菜单详情")
    private MenuData data;

    @ApiModelProperty(value = "子菜单列表")
    private List<MenuNode> children;
}
