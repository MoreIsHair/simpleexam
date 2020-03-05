package cn.tycad.oa.exam.controller;

import cn.tycad.oa.exam.annotation.RequiredPermission;
import cn.tycad.oa.exam.common.PermissionConstants;
import cn.tycad.oa.exam.model.bo.AuthNode;
import cn.tycad.oa.exam.model.entity.SystemRole;
import cn.tycad.oa.exam.model.param.UserRoleParam;
import cn.tycad.oa.exam.model.vo.Result;
import cn.tycad.oa.exam.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @Author: Shizf
 * @Date: 090307
 * @Description: 角色，权限 Controller
 */
@RestController
@RequestMapping("/role")
@Api("用户角色权限")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequiredPermission(PermissionConstants.ROLE_MANAGER)
    @ApiOperation(value = "获取用户角色列表", httpMethod = "GET")
    @GetMapping("/{pageNum}/{pageSize}")
    public Result<List<SystemRole>> roleList(
            @ApiParam(name = "pageNum", value = "当前页码") @PathVariable("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页条数") @PathVariable("pageSize") int pageSize) {

        List<SystemRole> list = roleService.list(pageNum, pageSize);
        return Result.success(list);
    }

    @RequiredPermission(PermissionConstants.ROLE_MANAGER)
    @ApiOperation(value = "添加用户角色", httpMethod = "POST")
    @PostMapping
    public Result<SystemRole> add(
            @RequestBody @ApiParam(name="role", value = "用户角色") SystemRole role) {

        String uuid = UUID.randomUUID().toString();
        role.setRoleId(uuid);
        roleService.save(role);
        return Result.success(role);
    }

    @RequiredPermission(PermissionConstants.ROLE_MANAGER)
    @ApiOperation(value = "删除用户角色", httpMethod = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @ApiParam(name = "id", value = "角色id") @PathVariable String id) {

        roleService.delete(id);
        return Result.success();
    }

    @RequiredPermission(PermissionConstants.ROLE_MANAGER)
    @ApiOperation(value = "更新用户角色信息", httpMethod = "PUT")
    @PutMapping
    public Result<SystemRole> update(@ApiParam(name="role", value = "用户角色") @RequestBody SystemRole role) {
        roleService.update(role);
        return Result.success();
    }

    @PutMapping("/userRole")
    public Result updateUserRole(@ApiParam(name = "roleInfo", value = "用户角色信息") @RequestBody UserRoleParam roleInfo) {
        roleService.updateUserRole(roleInfo);

        return Result.success();
    }

    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation(value = "查询用户角色详情", httpMethod = "GET")
    @GetMapping("/{id}")
    public Result<SystemRole> detail(
            @ApiParam(name = "id", value = "角色id") @PathVariable String id) {
        SystemRole role = roleService.getOne(id);
        return Result.success(role);
    }

    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation(value = "用户角色权限", httpMethod = "GET")
    @GetMapping(value="/roleAuth/{id}")
    public Result<List<AuthNode>> roleAuth(
            @ApiParam(name = "id", value = "角色id") @PathVariable String id) {

        List<AuthNode> result = roleService.getRoleAuth(id);
        return Result.success(result);
    }

    @RequiredPermission(PermissionConstants.ROLE_MANAGER)
    @ApiOperation(value = "更新用户角色权限", httpMethod = "PUT")
    @PutMapping(value="/roleAuth", produces = "application/json")
    public Result<Void> updateRoleAuth(
            @ApiParam(name = "id", value = "角色id") String roleId,
            @ApiParam(name = "id", value = "角色id") List<AuthNode> data) {

        roleService.updateRoleAuth(roleId, data);
        return Result.success();
    }
}
