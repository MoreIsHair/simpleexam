package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.SystemAuth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: shizf
 * @Date: 0415
 * @Description: 权限树节点
 */
@Data
@ApiModel(value = "AuthNode", description = "权限树节点")
public class AuthNode {
    @ApiModelProperty(value = "id，即权限id")
    private String nodeId;
    @ApiModelProperty(value = "父级权限id")
    private String parentId;
    @ApiModelProperty(value = "权限名称,如system_manager")
    private String name;
    @ApiModelProperty(value = "显示的名称")
    private String showText;
    @ApiModelProperty(value = "用户是否有该权限")
    private Boolean belongTo;
    @ApiModelProperty(value = "子权限列表")
    private List<AuthNode> children;

    public AuthNode(SystemAuth auth) {
        this.nodeId = auth.getAuthId();
        this.name = auth.getName();
        this.showText = auth.getShowText();
    }
}
