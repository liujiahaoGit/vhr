package com.cicro.vhr.service;

import com.cicro.vhr.mapper.DepartmentMapper;
import com.cicro.vhr.model.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DeptService {

    @Autowired
    private DepartmentMapper departmentMapper;

    /*
     * @methodName: queryAllDept
     * @description 递归查询全部部门树
     * @param:
     * @return:
     * @createdAt 19:06 2020/7/27 0027
     * @version 1.0.0
     **/
    public List<Department> queryAllDept() {
        return departmentMapper.queryAllDeptByPid(-1);
    }

    /*
     * @methodName: addDept
     * @description 调用存储过程完成添加部门的一系列操作
     * @param:
     * @return:
     * @createdAt 15:12 2020/7/28 0028
     * @version 1.0.0
     **/
    public void addDept(Department department) {
        department.setEnabled(true);
        departmentMapper.addDept(department);
    }

    public List<Department> queryAllDeptName() {
        return departmentMapper.queryAllDeptName();
    }

    public void delDeptById(Department department) {
        departmentMapper.delDeptById(department);
    }


}
