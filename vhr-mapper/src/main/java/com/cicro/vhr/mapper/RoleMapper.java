package com.cicro.vhr.mapper;

import com.cicro.vhr.model.Role;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> getRoleListByHrId(Integer id);

    List<Role> queryRoles();

    List<Integer> queryMenusIdByRoleId(Integer rid);

    List<Role> getAllRoleByHrId(Integer id);
}