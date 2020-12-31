package com.cicro.vhr.mapper;

import com.cicro.vhr.model.Menu;

import java.util.List;

public interface MenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<Menu> loadMenuTreeByHrId(Integer id);

    List<Menu> getAllRolesByMenus();

    List<Menu> getMenusTree();
}