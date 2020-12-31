package com.cicro.vhr.service;

import com.cicro.vhr.mapper.MenuMapper;
import com.cicro.vhr.mapper.MenuRoleMapper;
import com.cicro.vhr.model.Hr;
import com.cicro.vhr.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    public List<Menu> loadMenuTreeByHrId() {
        //根据当前登录用户ID查询当前用户所能看到的菜单资源
        return menuMapper.loadMenuTreeByHrId(((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

    }

    public List<Menu> getAllRolesByMenus() {
        return menuMapper.getAllRolesByMenus();

    }

    public List<Menu> getMenusTree() {
        return menuMapper.getMenusTree();

    }

    @Transactional(rollbackFor = Exception.class)
    public Integer updateMenuByRid(Integer rid, Integer[] mids) {
        menuRoleMapper.deleteByRid(rid);
        Integer integer = menuRoleMapper.insertRecod(rid, mids);
        return integer;

    }
}
