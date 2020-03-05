package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.SystemDepartment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/6
 * @Description:
 */
@Mapper
public interface SystemDepartmentMapper {
    /**
     * 新增一个部门
     * @param department 部门信息
     */
    void insert(SystemDepartment department);

    /**
     * 获取部门列表
     * @return
     */
    List<SystemDepartment> getList();

    /**
     * 更新部门信息
     * @param department 部门信息
     */
    void update(SystemDepartment department);
}
