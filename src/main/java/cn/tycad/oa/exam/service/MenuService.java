package cn.tycad.oa.exam.service;

import cn.tycad.oa.exam.common.util.CommonUtils;
import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.model.entity.SystemMenu;
import cn.tycad.oa.exam.repository.SystemMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class MenuService {

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    /**
     * 获取单个菜单信息
     * @param id 菜单id
     * @return 菜单信息
     */
    public SystemMenu getOne(String id) {
        return systemMenuMapper.getOne(id);
    }

    /**
     * 根据角色获取用户菜单
     * @param roleIds 角色id
     * @return 用户菜单列表
     */
    public List<SystemMenu> getByRole(String[] roleIds) {
        List<String> list = CommonUtils.arrayToList(roleIds);
        return systemMenuMapper.getByRole(list);
    }

    /**
     * 获取菜单列表
     * @return 菜单列表
     */
    public List<SystemMenu> getList() {
        return systemMenuMapper.getList();
    }

    /**
     * 插入菜单信息
     * @param menu 菜单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(SystemMenu menu) {
        int count = systemMenuMapper.getByParentIdAndName(menu.getParentMenuId(), menu.getMenuName());
        if (count > 0) {
            throw new BusinessException(ExceptionInfoEnum.MENU_NAME_DUPLICATE);
        }
        systemMenuMapper.insert(menu);
    }

    /**
     * 删除菜单信息
     * @param id 菜单id
     */
    public void delete(String id) {
        systemMenuMapper.delete(id);
    }
}
