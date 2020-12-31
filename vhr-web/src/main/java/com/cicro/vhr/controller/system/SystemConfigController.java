package com.cicro.vhr.controller.system;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.model.Menu;
import com.cicro.vhr.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/config")
@Slf4j
public class SystemConfigController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/menuTree")
    public Result login() {

        log.info("查询菜单树开始....");
        long startTime = System.currentTimeMillis();
        Result result = new Result(ResultCode.OK);
        List<Menu> menus = menuService.loadMenuTreeByHrId();
        result.setData(menus);
        long endTime = System.currentTimeMillis();
        log.info("查询菜单树结束,共耗时{}ms....", (startTime - endTime));
        return result;
    }

}
