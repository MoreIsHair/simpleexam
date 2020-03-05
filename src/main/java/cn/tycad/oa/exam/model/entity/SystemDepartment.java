package cn.tycad.oa.exam.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/3/7
 * @Description: 部门模型
 */
@Data
@ApiModel(value = "SystemDepartment", description = "部门模型")
public class SystemDepartment {
    @ApiModelProperty(value = "部门id")
    private String deptId;
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    @ApiModelProperty(value = "说明")
    private String deptDescription;
}
