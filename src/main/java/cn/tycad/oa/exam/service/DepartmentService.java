package cn.tycad.oa.exam.service;

import cn.tycad.oa.exam.model.bo.DepartmentAndGroupBO;
import cn.tycad.oa.exam.model.entity.SystemDepartment;
import cn.tycad.oa.exam.model.entity.SystemGroup;
import cn.tycad.oa.exam.repository.SystemDepartmentMapper;
import cn.tycad.oa.exam.repository.SystemGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author: shizf
 * @Date: 0409
 * @Description: 部门service
 */
@Service
public class DepartmentService {
    @Autowired
    private SystemDepartmentMapper departmentMapper;

    @Autowired
    private SystemGroupMapper groupMapper;

    /**
     * 获取部门树
     * @return
     */
    public List<DepartmentAndGroupBO> getDepartmentTree() {
        List<SystemDepartment> departments = departmentMapper.getList();
        List<SystemGroup> groups = groupMapper.getList();

        return departments.stream()
                .map(dep -> new DepartmentAndGroupBO(dep.getDeptId()
                        , dep.getDeptName()
                        , "-1"
                        , getDepChildren(dep.getDeptId(), groups)
                        , new ArrayList<>()))
                .collect(toList());
    }

    /**
     * 获取部门分组
     * @param depId 部门id
     * @param groups 所有分组
     * @return
     */
    private List<DepartmentAndGroupBO> getDepChildren(String depId, List<SystemGroup> groups) {
        return groups.stream()
                .filter(group -> group.getDepartmentId().equals(depId))
                .map(group ->
                        new DepartmentAndGroupBO(group.getGroupId()
                                , group.getGroupName()
                                , group.getDepartmentId()
                                , new ArrayList<DepartmentAndGroupBO>()
                                , new ArrayList<>()))
                .collect(toList());
    }

    /**
     * 获取所有部门
     * @return
     */
    public List<SystemDepartment> list() {
        return departmentMapper.getList();
    }

    /**
     * 写入部门信息
     */
    public void insert(SystemDepartment department) {
        departmentMapper.insert(department);
    }

    /**
     * 修改部门信息
     * @param department 部门信息
     */
    public void update(SystemDepartment department) {
        departmentMapper.update(department);
    }
}
