package com.cicro.vhr.controller.system.basic;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.model.Menu;
import com.cicro.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/basic/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public Result queryRoles() {

        List<Menu> menusTree = menuService.getMenusTree();
        Result result = new Result(ResultCode.SUCCESS);
        result.setMessage(null);
        result.setData(menusTree);
        return result;
    }

    /*
     * @methodName:
     * @description 修改权限 根据角色ID先删除当前角色拥有的所有权限,然后再根据传入的权限id增加权限
     * @param:
     * @return:
     * @createdAt 15:32 2020/7/27 0027
     * @version 1.0.0
     **/
    @PutMapping
    public Result updateMenuByRid(Integer rid, Integer[] mids) {

        Integer integer = menuService.updateMenuByRid(rid, mids);
        Result result = null;
        if (integer != -1) {
            result = new Result(ResultCode.SUCCESS);
            result.setMessage("修改成功");
            return result;
        }
        result = new Result(ResultCode.FAIL);
        result.setMessage("修改失败");
        return result;

    }

}
