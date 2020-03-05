package cn.tycad.oa.exam.model.entity;

import cn.tycad.oa.exam.model.bo.SystemUserBo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YY
 * @Date: 2019/3/4 17:04
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "SystemUser", description = "用户模型")
public class SystemUser implements Serializable {
    private static final long serialVersionUID = -4685991165662176047L;

    @ApiModelProperty(value = "用户id")
    protected String userId;
    @ApiModelProperty(value = "用户名，登录名（唯一）")
    protected String username;
    @ApiModelProperty(value = "用户姓名，真实姓名（中文）")
    protected String name;
    @ApiModelProperty(value = "部门id")
    protected String deptId;
    @ApiModelProperty(value = "用户密码")
    protected String password;
    @ApiModelProperty(value = "用户说明")
    protected String description;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    protected Date lastEditTime;
    @ApiModelProperty(value = "性别，字符串“男”或“女”")
    protected String gender;
    @ApiModelProperty(value = "用户头像地址")
    protected String proFilePicture;
    @ApiModelProperty(value = "用户生日")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    protected Date birthday;
    @ApiModelProperty(value = "电话号码")
    private String tel;
    @ApiModelProperty(value = "用户组id")
    protected String groupId;
    @ApiModelProperty(value = "用户组名")
    protected String groupName;
    @ApiModelProperty(value = "用户公司名称")
    protected String companyName;
    @ApiModelProperty(value = "用户类型（0内部，1外包）")
    protected Integer type;

    public SystemUser(SystemUserBo sub) {
        this.setGroupId(sub.getGroupId());
        this.setLastEditTime(sub.getLastEditTime());
        this.setDeptId(sub.getDeptId());
        this.setCreateTime(sub.getCreateTime());
        this.setBirthday(sub.getBirthday());
        this.setGroupName(sub.getGroupName());
        this.setDescription(sub.getDescription());
        this.setGender(sub.getGender());
        this.setGroupId(sub.getGroupId());
        this.setPassword(null);
        this.setUsername(sub.getUsername());
        this.setName(sub.getName());
        this.setUserId(sub.getUserId());
        this.setTel(sub.getTel());
        this.setType(sub.getType());
        this.setProFilePicture(sub.getProFilePicture());
        this.setCompanyName(sub.getCompanyName());
    }
}
