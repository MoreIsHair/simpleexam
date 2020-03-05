package cn.tycad.oa.exam.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: shizf
 * @Date: 0409
 * @Description: 部门树节点模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "部门节点模型")
public class DepartmentAndGroupBO {
    @ApiModelProperty(value = "id，即部门或用户组id")
    String id;

    @ApiModelProperty(value = "部门或用户组名称")
    String name;

    @ApiModelProperty(value = "父级节点id，如果是根节点，则为“-1”")
    String parentId;

    @ApiModelProperty(value = "子节点列表")
    List<DepartmentAndGroupBO> children;

    @ApiModelProperty(value = "用户列表")
    List<ExamUserBo> users;
}
