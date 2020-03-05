package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.SystemAuth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Shizf
 * @Date: 190308
 * @Description: 权限接口
 */
@Mapper
public interface SystemAuthMapper {
    /**
     * 查询所有的权限
     * @return 所有权限的集合
     */
    List<SystemAuth> selectList();

    /**
     * 根据角色id获取权限列表
     * @param id 角色id
     * @return 权限列表
     */
    List<SystemAuth> selectByRoleId(String id);

    /**
     * 删除角色所有权限
     * @param id 角色id
     */
    void deleteAll(String id);

    void update(String authId);
}
