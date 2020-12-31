package com.cicro.vhr.controller.system.basic;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.model.Department;
import com.cicro.vhr.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/basic/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping
    public Result queryAllDept() {

        List<Department> departments = deptService.queryAllDept();
        Result result = new Result(ResultCode.SUCCESS);
        result.setMessage("查询成功");
        result.setData(departments);
        return result;
    }

    @GetMapping("/queryAllDeptName")
    public Result queryAllDeptName() {

        List<Department> departments = deptService.queryAllDeptName();
        Result result = new Result(ResultCode.OK);
        result.setData(departments);
        return result;
    }

    @PostMapping
    public Result addDept(@RequestBody Department department) {
        deptService.addDept(department);
        if (department.getResult() == 1) {
            return Result.SUCCESS("添加成功");

        }
        return Result.FAIL("添加失败");

    }

    @DeleteMapping("/{id}")
    public Result delDeptById(@PathVariable Integer id) {
        Department department = new Department();
        department.setId(id);
        deptService.delDeptById(department);
        if (department.getResult()==-2){
            return Result.FAIL("该部门是父部门,不能直接删除");
        }else if (department.getResult()==-1){
            return Result.FAIL("该部门下有所属员工,不能直接删除");
        }else if(department.getResult()==1){
            return Result.SUCCESS("删除成功");
        }else {
            return Result.FAIL("删除失败");
        }
    }

    /*@GetMapping("/{rid}")
    public Result queryMenusByRoleId(@PathVariable("rid") Integer id) {

        List<Integer> mIds = roleService.queryMenusIdByRoleId(id);
        Result result = new Result(ResultCode.SUCCESS);
        result.setMessage("查询成功");
        result.setData(mIds);
        return result;
    }

    @PostMapping
    public Result addRoles(@RequestBody Role role) {
        roleService.addRole(role);
        Result result = new Result(ResultCode.SUCCESS);
        result.setMessage("添加成功");
        return result;

    }

    */
/*
    @PutMapping
    public Result updateRoles(@RequestBody JobLevel jobLevel) {
        return roleService.updateJobLevel(jobLevel);
    }

   */

}
