package com.cicro.vhr.controller.system.basic;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.model.Role;
import com.cicro.vhr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/basic/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public Result queryRoles() {

        return roleService.queryRole();
    }

    @GetMapping("/{rid}")
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

    @DeleteMapping
    public Result delRolesById(@RequestParam("ids") Integer id) {
        Integer integer = roleService.delRoleById(id);
        Result result =null;
        if (integer==1){
            result=new Result(ResultCode.SUCCESS);
            result.setMessage("删除成功");
            return result;
        }
        result=new Result(ResultCode.FAIL);
        result.setMessage("删除失败");
        return result;
    }
/*
    @PutMapping
    public Result updateRoles(@RequestBody JobLevel jobLevel) {
        return roleService.updateJobLevel(jobLevel);
    }

   */

}
