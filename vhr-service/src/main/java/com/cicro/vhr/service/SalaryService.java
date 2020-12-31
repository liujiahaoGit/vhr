package com.cicro.vhr.service;

import com.cicro.vhr.mapper.SalaryMapper;
import com.cicro.vhr.model.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SalaryService {

    @Autowired
    private SalaryMapper salaryMapper;

    public List<Salary> getAllSalary() {
        return salaryMapper.getAllSalary();
    }

    public Integer saveSalary(Salary salary) {
        salary.setCreateDate(new Date());
        return salaryMapper.insertSelective(salary);
    }

    public Integer deleteSalaryById(Integer id) {
        return salaryMapper.deleteByPrimaryKey(id);
    }

    public Integer updateSalary(Salary salary) {
        return salaryMapper.updateByPrimaryKeySelective(salary);
    }

    public Integer deleteSalaryByIds(Integer[] ids) {
        return salaryMapper.deleteSalaryByIds(ids);
    }
}
