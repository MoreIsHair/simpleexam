package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.SystemGroup;
import cn.tycad.oa.exam.model.entity.SystemUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: shizf
 * @Date: 20190408
 * @Description: 用户组mapper
 */
@Mapper
public interface SystemGroupMapper {
    /**
     * 插入用户组
     * @param systemGroup
     */
    void insert(SystemGroup systemGroup);

    /**
     * 获取用户组列表
     * @return
     */
    List<SystemGroup> getList();

    /**
     * 根据分组id获取用户列表
     * @param groupId 分组id
     * @return
     */
    List<SystemUser> getUsersByGroupId(String groupId);
}
