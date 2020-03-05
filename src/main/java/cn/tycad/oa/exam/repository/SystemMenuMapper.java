package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.SystemMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemMenuMapper {

    /**
     * 获取单个菜单信息
     * @param id 菜单id
     * @return 菜单信息
     */
    SystemMenu getOne(String id);

    /**
     * 获取所有菜单信息
     * @return
     */
    List<SystemMenu> getList();

    /**
     * 插入菜单信息
     * @param menu 菜单信息
     */
    void insert(SystemMenu menu);

    /**
     * 更新菜单信息
     * @param menu 菜单信息
     */
    void update(SystemMenu menu);

    /**
     * 删除菜单信息
     * @param id 菜单id
     */
    void delete(String id);

    /**
     * 根据parentmenuid和menuname获取当前的条数
     * @param parentMenuId 父级菜单id
     * @param menuName 菜单名称
     * @return 数据库中存在的条数
     */
    int getByParentIdAndName(String parentMenuId, String menuName);

    /**
     * 根据角色获取用户菜单
     * @param roleIds 角色id
     * @return 菜单列表
     */
    List<SystemMenu> getByRole(List<String> roleIds);
}
