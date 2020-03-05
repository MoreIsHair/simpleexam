package cn.tycad.oa.exam.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: shizf
 * @Date: 0422
 * @Description: 用户列表搜索参数
 */
@Data
@ApiModel
public class UserParam {
    @ApiModelProperty(value = "用户姓名")
    private String name;
}
