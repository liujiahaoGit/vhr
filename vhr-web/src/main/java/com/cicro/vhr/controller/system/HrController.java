package com.cicro.vhr.controller.system;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.model.Hr;
import com.cicro.vhr.model.Role;
import com.cicro.vhr.service.RoleService;
import com.cicro.vhr.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/hr")
public class HrController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public Result getAllRoleByHrId(String searchWord) {
        List<Hr> list = userService.getAllRoleByHrId(searchWord);
        return Result.SUCCESS(list);

    }

    @PutMapping
    public Result updateHrById(@RequestBody Hr hr) {
        Integer integer = userService.updateHrById(hr);
        if (integer == 1) {

            return Result.SUCCESS("修改成功");
        }

        return Result.FAIL("修改失败");
    }

    @GetMapping("/roles")
    public Result getAllRoles() {
        return roleService.queryRole();
    }

    @GetMapping("/roles/{id}")
    public Result getAllRolesByHrId(@PathVariable Integer id) {
        List<Role> roles = userService.getAllRolesByHrId(id);
        return Result.SUCCESS(roles);
    }

    @PutMapping("/hrRole")
    public Result updateHrRoleById(Integer hid, Integer[] rids) {
        boolean flag = userService.updateHrRoleById(hid, rids);
        if (flag) {

            return Result.SUCCESS("修改成功");
        }

        return Result.FAIL("修改失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteHrById(@PathVariable Integer id) {
        Integer result = userService.deleteHrById(id);
        if (result==1){
            return Result.SUCCESS("删除成功");
        }
        return Result.FAIL("删除失败");

    }


}
