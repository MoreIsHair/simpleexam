package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.SystemRole;
import cn.tycad.oa.exam.model.entity.SystemUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: shizf
 * @Date: 0422
 * @Description: 用户信息模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserBo extends SystemUser {
    @ApiModelProperty(value = "用户所属角色")
    List<SystemRole> roles;
    @ApiModelProperty(value = "用户所在部门")
    private String department;
}
