package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.bo.UserRoleBo;
import cn.tycad.oa.exam.model.entity.SystemRole;
import cn.tycad.oa.exam.model.param.UserRoleParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/5
 * @Description:
 */
@Mapper
public interface SystemRoleMapper {

    /**
     * 新增一条角色
     * @param systemRole 角色信息
     */
    void insert(SystemRole systemRole);

    /**
     * 查询所有的角色
     * @return 所有角色的集合
     */
    List<SystemRole> selectList(int pageNum, int pageSize);

    /**
     * 查询所有角色
     * @return
     */
    List<SystemRole> list();

    /**
     * 删除一条角色(仅仅只是把角色表中的一条记录给删除)
     * @param roleId 角色id
     */
    void deleteByRoleId(String roleId);

    /**
     * 根据角色名称获取角色信息
     * @param roleName 角色名称
     * @return 角色信息列表
     */
    List<SystemRole> getByName(String roleName, String roleId);

    /**
     * 查询一条角色信息
     * @param id 角色id
     * @return 角色信息
     */
    SystemRole getOne(String id);

    /**
     * 根据用户名列表查找用户角色信息
     * @param userIds
     * @return
     */
    List<UserRoleBo> getByUserIds(@Param("userIds") List<String> userIds);

    /**
     * 根据用户名查找用户角色信息
     * @param userId
     * @return
     */
    UserRoleBo getByUserId(@Param("userId") String userId);

    /**
     * 更新角色信息
     * @param role
     */
    void update(SystemRole role);

    /**
     * 更新用户角色信息
     * @param roleInfo
     */
    void updateUserRole(UserRoleParam roleInfo);

    /**
     * 清空Role表
     */
    void deleteAllRoleInfo();
}
