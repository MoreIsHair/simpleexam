package cn.tycad.oa.exam.controller;

import cn.tycad.oa.exam.annotation.RequiredPermission;
import cn.tycad.oa.exam.common.PermissionConstants;
import cn.tycad.oa.exam.model.bo.DepartmentAndGroupBO;
import cn.tycad.oa.exam.model.bo.SystemUserBo;
import cn.tycad.oa.exam.model.entity.SystemUser;
import cn.tycad.oa.exam.model.param.UserNameAndPasswordParam;
import cn.tycad.oa.exam.model.vo.Result;
import cn.tycad.oa.exam.service.DepartmentService;
import cn.tycad.oa.exam.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author YY
 * @date 2019/3/5 9:45
 * @description
 */
@RestController
@RequestMapping(value = "/user")
@Api("用户管理")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @RequiredPermission(PermissionConstants.USER_MANAGER)
    @ApiOperation(value = "分页获取用户列表")
    @GetMapping("/{pageNum}/{pageSize}")
    public Result<PageInfo<SystemUserBo>> list(
            @ApiParam(name = "pageNum", value = "页码", defaultValue = "1") @PathVariable("pageNum") int pageNum,
            @ApiParam(name = "pageSize", value = "每页条数", defaultValue = "15") @PathVariable("pageSize") int pageSize,
            @ApiParam(name = "name", value = "查询条件") @RequestParam(name = "name", required = false) String name
    ) {
        PageInfo<SystemUserBo> list = new PageInfo<>();
        if (StringUtils.isNotBlank(name) && name.matches("[a-zA-Z]+")){
            list = userService.list(pageNum, pageSize, name,null);
        }else {
            list = userService.list(pageNum, pageSize,null, name);
        }
        return Result.success(list);
    }

    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation(value = "用户详情", httpMethod = "GET")
    @GetMapping("/{id}")
    public Result<SystemUser> detail(
            @ApiParam(name = "id", value = "用户ID") @PathVariable("id") String id) {
        SystemUser user = userService.getOne(id);
        return Result.success(user);
    }

    @RequiredPermission(PermissionConstants.USER_MANAGER)
    @ApiOperation(value = "新增用户", httpMethod = "POST")
    @PostMapping
    public Result<SystemUser> add(@ApiParam(name="user", value = "用户信息") SystemUser user) {
        String uuid = UUID.randomUUID().toString();
        user.setUserId(uuid);
        return Result.success(user);
    }

    @RequiredPermission(PermissionConstants.LOGIN)
    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public Result login(
            @ApiParam(name = "userNameAndPasswordParam",value = "用户名、密码",required = true)
            @RequestBody UserNameAndPasswordParam userNameAndPasswordParam){
        Map login = userService.login(userNameAndPasswordParam);
        return Result.success(login);
    }

    @RequiredPermission(PermissionConstants.LOGIN)
    @ApiOperation("更新密码")
    @PutMapping("/password")
    public Result updatePassword(HttpServletRequest request,
                                 @ApiParam(name = "userNameAndPasswordParam",value = "用户名、密码",required = true)
                                 @RequestBody UserNameAndPasswordParam userNameAndPasswordParam
    ) {
        String newPass = userNameAndPasswordParam.getPassword();
        SystemUser user = userService.updatePassword(request, newPass);
        return Result.success(user);
    }

    @RequiredPermission(PermissionConstants.REGISTER)
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result register(
            @ApiParam(name = "userNameAndPasswordParam",value = "用户名、密码",required = true)
            @RequestBody UserNameAndPasswordParam userNameAndPasswordParam){
        userService.registerUser(userNameAndPasswordParam);
       return Result.success();
    }

    @RequiredPermission(PermissionConstants.USER_MANAGER)
    @ApiOperation("导入用户信息")
    @PostMapping("/fileUpload")
    public Result importUsers (@ApiParam(name = "fileName",value = "上传的excel文件")
                                   @RequestParam(value = "fileName") MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) {
            return Result.error("文件为空");
        }
        // 先把文件存起来
        String fileName = file.getOriginalFilename();
        String basePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "temp" + System.getProperty("file.separator");
        File temp = new File(basePath);
        File dest = new File((basePath + fileName));
        if (!temp.exists()) {
            temp.mkdirs();
        }
        if (dest.exists()) {
            dest.delete();
        }
        file.transferTo(dest);
        userService.bulkInsertUser(dest);

        // 删除文件夹
        if (temp.exists()){
            // 删除文件
            if (dest.exists()){
                dest.delete();
            }
            temp.delete();
        }
        return Result.success();
    }

    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation("注销登陆")
    @GetMapping("/logout")
    public Result<Void> logout(HttpServletRequest request){
        userService.logout(request);
        return Result.success();
    }

    @RequiredPermission(PermissionConstants.USER_MANAGER)
    @ApiOperation("根据分组ID获取用户列表")
    @GetMapping("/group/{id}")
    public Result<List<SystemUser>> getUserListByGroup(
            @ApiParam(name = "id", value = "分组ID") @PathVariable("id") String id) {
        List<SystemUser> users = userService.getUsersByGroupId(id);
        return Result.success(users);
    }

    @RequiredPermission(PermissionConstants.USER_MANAGER)
    @ApiOperation("获取部门和分组树")
    @GetMapping("/departments")
    public Result<List<DepartmentAndGroupBO>> getDepartmentsAndGroups() {
        List<DepartmentAndGroupBO> result = departmentService.getDepartmentTree();
        return Result.success(result);
    }

    @ApiOperation("初始化密码（改为用户名）")
    @GetMapping("/initPassword/{username}")
    public Result<Void> initPassword(@PathVariable(value = "username") String username) {
        userService.initPassword(username);
        return Result.success();
    }
}
