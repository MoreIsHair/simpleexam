package cn.tycad.oa.exam.controller;

import cn.tycad.oa.exam.annotation.RequiredPermission;
import cn.tycad.oa.exam.common.PermissionConstants;
import cn.tycad.oa.exam.model.bo.MenuData;
import cn.tycad.oa.exam.model.bo.MenuNode;
import cn.tycad.oa.exam.model.entity.SystemMenu;
import cn.tycad.oa.exam.model.vo.Result;
import cn.tycad.oa.exam.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu")
@Api("角色菜单")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation(value = "查询角色菜单", httpMethod = "GET")
    @GetMapping("/{roleIds}")
    public Result<List<MenuNode>> menus(
            @ApiParam(name = "roleIds", value = "角色id") @PathVariable("roleIds") String roleIds) {

        String[] idParams = roleIds.split(",");
        if (idParams.length < 1) {return Result.success(new ArrayList<>());}
        List<SystemMenu> menus = menuService.getByRole(idParams);
        List<MenuNode> result = new ArrayList<>();

        for (SystemMenu menu : menus) {
            if ("-1".equals(menu.getParentMenuId())) {
                //顶层菜单
                int menuLevel = 1;

                MenuNode node = buildMenuNode("-1", menu, menuLevel, menus);
                result.add(node);
            }
        }

        return Result.success(result);
    }

    @ApiOperation(value = "新增菜单", httpMethod = "POST")
    @PostMapping
    public Result<Void> insert(@Valid @RequestBody SystemMenu menu, Errors errors) {
        if (errors.hasErrors()) {
            return Result.error("参数不合法");
        }
        String id = UUID.randomUUID().toString();
        menu.setMenuId(id);
        menuService.insert(menu);

        return Result.success();
    }

    /**
     * 构造子菜单
     * @param menuLevel
     * @param menuId
     * @param menus
     * @return
     */
    private List<MenuNode> getMenuChildren(int menuLevel, String menuId,
                                           List<SystemMenu> menus) {
        if (menus.size() == 0) {
            return new ArrayList<>();
        } else {
            List<MenuNode> result = new ArrayList<>();
            menuLevel += 1;
            for (SystemMenu menu : menus) {
                if (menu.getParentMenuId().equals(menuId)) {
                    MenuNode node = buildMenuNode(menuId, menu, menuLevel, menus);
                    result.add(node);
                }
            }
            return result;
        }
    }

    /**
     * 构造菜单节点
     * @param menu
     * @param menuLevel
     * @param menus
     * @return
     */
    private MenuNode buildMenuNode(String parentId, SystemMenu menu, int menuLevel,
                                   List<SystemMenu> menus) {
        MenuData data = new MenuData();
        data.setName(menu.getMenuName());
        data.setSort(menu.getSortOrder());
        data.setExtend("extends");
        data.setCode(menu.getMenuName());
        data.setPath(menu.getPath());

        MenuNode node = new MenuNode();
        node.setParentId(parentId);
        node.setLevel(String.valueOf(menuLevel));
        node.setId(menu.getMenuId());
        node.setData(data);

        List<MenuNode> children = getMenuChildren(menuLevel, menu.getMenuId(), menus);
        node.setChildren(children);
        return node;
    }
}
