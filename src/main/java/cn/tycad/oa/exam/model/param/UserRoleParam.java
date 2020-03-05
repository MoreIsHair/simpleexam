package cn.tycad.oa.exam.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: shizf
 * @Date: 0424
 * @Desciption: 修改用户角色接口参数
 */
@Data
public class UserRoleParam {
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "角色id列表")
    private List<String> roleIds;
}
