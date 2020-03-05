package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.SystemRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: shizf
 * @Date: 0423
 * @Description: 查询用户角色模型
 */
@Data
public class UserRoleBo {
    @ApiModelProperty(value = "用户角色模型")
    private String userId;

    @ApiModelProperty(value = "用户角色")
    private List<SystemRole> roles;
}
