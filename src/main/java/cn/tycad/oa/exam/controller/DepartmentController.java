package cn.tycad.oa.exam.controller;

import cn.tycad.oa.exam.model.entity.SystemDepartment;
import cn.tycad.oa.exam.model.vo.Result;
import cn.tycad.oa.exam.service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shizf
 * @date 0409
 * @description 部门controller
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation("获取所有部门")
    @GetMapping
    public Result<List<SystemDepartment>> list() {
        List<SystemDepartment> result = departmentService.list();
        return Result.success(result);
    }

    @ApiOperation("新增部门")
    @PostMapping
    public Result insert(@ApiParam(value = "department", name = "部门信息")
                           @RequestBody SystemDepartment department) {
        departmentService.insert(department);

        return Result.success();
    }

    @ApiOperation("修改部门信息")
    @PutMapping
    public Result update(
            @ApiParam(value = "部门信息", name = "department") @RequestBody SystemDepartment department) {
        departmentService.update(department);
        return Result.success();
    }
}
