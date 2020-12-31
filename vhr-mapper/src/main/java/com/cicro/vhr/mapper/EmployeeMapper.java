package com.cicro.vhr.mapper;

import com.cicro.vhr.model.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    List<Employee> getAllEmployee(@Param("emp") Employee emp, @Param("beginDate") Date[] beginDate);

    String getWorkId();

    int batchInsert(LinkedList<Employee> newList);

    Employee getEmployeeById(Integer id);

    List<Employee> getAllEmpWithSalary();

    Integer updateSalaryById(@Param("eid") Integer eid,@Param("sid") Integer sid);
}