package cn.tycad.oa.exam.service;


import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.model.bo.AuthNode;
import cn.tycad.oa.exam.model.entity.SystemAuth;
import cn.tycad.oa.exam.model.entity.SystemRole;
import cn.tycad.oa.exam.model.entity.SystemUser;
import cn.tycad.oa.exam.model.param.UserRoleParam;
import cn.tycad.oa.exam.repository.SystemAuthMapper;
import cn.tycad.oa.exam.repository.SystemRoleMapper;
import cn.tycad.oa.exam.repository.SystemUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Shizf
 * @Date: 190307
 * @Description: 角色，权限 Controller
 */
@Service
@Slf4j
public class RoleService {

    @Autowired
    private SystemRoleMapper roleMapper;

    @Autowired
    private SystemAuthMapper authMapper;

    @Autowired
    private SystemUserMapper userMapper;

    /**
     * 查询角色列表
     * @return 角色列表
     */
    public List<SystemRole> list(int pageNum, int pageSize) {
        return roleMapper.selectList(pageNum, pageSize);
    }

    /**
     * 新增用户角色
     * @param role 用户角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(SystemRole role) {
        checkRoleName(role.getRoleName(), role.getRoleId());
        roleMapper.insert(role);
    }

    /**
     * 更新角色信息
     * @param role 角色信息
     */
    public void update(SystemRole role) {
        checkRoleName(role.getRoleName(), role.getRoleId());
        roleMapper.update(role);
    }

    /**
     * 删除用户角色
     * @param id 角色id
     */
    public void delete(String id) {

        if (id.isEmpty()) {
            log.error("【删除用户角色信息】id不能为空");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        List<SystemUser> users = userMapper.getByRoleId(id);
        if (users.size() > 0) {
            log.error("【删除角色信息错误，无法删除已分配用户的角色】id:%s", id);
            throw new BusinessException(ExceptionInfoEnum.INVALID_DELETE);
        }
        roleMapper.deleteByRoleId(id);
    }

    /**
     * 更新角色权限
     * @param roleId 角色id
     * @param data 数据
     */
    public void updateRoleAuth(String roleId, List<AuthNode> data) {
        if (roleId.isEmpty()) {
            log.error("【更新角色权限】id不能为空");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }

        if (data.size() == 0) {
            authMapper.deleteAll(roleId);
        } else {
            List<String> authIds = getAuthIdListByTreeData(data);
            if (authIds.size() == 0) {
                authMapper.deleteAll(roleId);
            } else {
                authMapper.deleteAll(roleId);
                for (String id: authIds) {
                    authMapper.update(id);
                }
            }
        }
    }

    /**
     * 查询单个用户角色信息
     * @param id 角色id
     * @return 角色信息
     */
    public SystemRole getOne(String id) {
        if (id.isEmpty()) {
            log.error("【id不能为空】");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }

        return roleMapper.getOne(id);
    }

    /**
     * 根据用户角色id获取角色所有权限
     * @param id 用户角色id
     * @return 所有权限列表
     */
    public List<AuthNode> getRoleAuth(String id) {

        List<SystemAuth> roleAuthList = authMapper.selectByRoleId(id);
        List<SystemAuth> authList = authMapper.selectList();

        List<AuthNode> tmp = authList.stream()
                .filter(map ->
                        roleAuthList.stream().anyMatch(map1 -> map.getAuthId().equals(map1.getAuthId())))
                .map(AuthNode::new)
                .collect(Collectors.toList());

        List<AuthNode> tmp1 = authList.stream()
                .filter(map ->
                        roleAuthList.stream().anyMatch(map1 -> !map.getAuthId().equals(map1.getAuthId())))
                .map(AuthNode::new)
                .collect(Collectors.toList());

        tmp.addAll(tmp1);

        return tmp;
    }

    /**
     * 检查角色名称是否重复
     * @param roleName 角色名称
     */
    private void checkRoleName(String roleName, String roleId) {
        List<SystemRole> roles = roleMapper.getByName(roleName, roleId);
        if (roles.size() > 0) {
            log.error("【角色名称不能重复】--%s", roleName);
            throw new BusinessException(ExceptionInfoEnum.ROLE_NAME_DUPLICATE);
        }
    }

    /**
     * 遍历权限的树形结构
     * @param data 权限树形结构
     * @return 拥有权限的id
     */
    private List<String> getAuthIdListByTreeData(List<AuthNode> data) {
        List<String> result = new ArrayList<>();
        for (AuthNode node: data) {
            if (node.getBelongTo()) {
                result.add(node.getNodeId());
            }

            result.addAll(getAuthIdListByTreeData(node.getChildren()));
        }

        return result;
    }

    /**
     * 更新用户角色
     * @param roleInfo
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(UserRoleParam roleInfo) {
        if (roleInfo.getRoleIds().size() == 0) {
            throw new BusinessException(ExceptionInfoEnum.EMPTY_USER_ROLE);
        }
        roleMapper.updateUserRole(roleInfo);
    }

    public void  deleteAllRoleInfo(){
        roleMapper.deleteAllRoleInfo();
    }
}
