package com.cicro.vhr.mapper;

import com.cicro.vhr.model.Hr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Hr record);

    int insertSelective(Hr record);

    Hr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hr record);

    int updateByPrimaryKey(Hr record);

    Hr loadUserByUsername(String s);

    List<Hr> getAllRoleByHrId(@Param("id") Integer id, @Param("searchWord") String searchWord);

    List<Hr> getAllHrIncludeCurrent(Integer id);

    Integer updatePass(@Param("hrId") Integer hrId, @Param("newPass") String newPass);
}