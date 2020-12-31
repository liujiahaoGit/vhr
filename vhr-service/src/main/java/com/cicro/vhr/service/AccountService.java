package com.cicro.vhr.service;

import com.cicro.vhr.entity.ResultPage;
import com.cicro.vhr.mapper.EmployeeMapper;
import com.cicro.vhr.model.Employee;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public ResultPage getAllEmpWithSalary(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Employee> employees = employeeMapper.getAllEmpWithSalary();
        PageInfo pageInfo = new PageInfo(employees);
        ResultPage resultPage = ResultPage.builder()
            .list(pageInfo.getList())
            .pageTotal(pageInfo.getPages())
            .pageSize(pageInfo.getPageSize())
            .currPage(pageInfo.getPageNum())
            .totalCount(pageInfo.getTotal())
            .build();
        return resultPage;
    }

    public Integer updateSalaryById(Integer eid, Integer sid) {

        return employeeMapper.updateSalaryById(eid, sid);
    }
}
