package com.cicro.vhr.mapper;

import com.cicro.vhr.model.HrRole;
import com.cicro.vhr.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HrRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HrRole record);

    int insertSelective(HrRole record);

    HrRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HrRole record);

    int updateByPrimaryKey(HrRole record);

    List<Role> getAllRoleByHrId(Integer id);

    void deleteByHrid(Integer hid);

    Integer insertRids(@Param("hid") Integer hid, @Param("rids") Integer[] rids);

}