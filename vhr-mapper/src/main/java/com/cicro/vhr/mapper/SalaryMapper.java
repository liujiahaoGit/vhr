package com.cicro.vhr.mapper;

import com.cicro.vhr.model.Salary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Salary record);

    int insertSelective(Salary record);

    Salary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Salary record);

    int updateByPrimaryKey(Salary record);

    List<Salary> getAllSalary();

    Integer deleteSalaryByIds(@Param("ids") Integer[] ids);
}