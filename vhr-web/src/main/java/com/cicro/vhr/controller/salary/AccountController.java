package com.cicro.vhr.controller.salary;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultPage;
import com.cicro.vhr.model.Salary;
import com.cicro.vhr.service.AccountService;
import com.cicro.vhr.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/salary/sobcfg")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private SalaryService salaryService;

    @GetMapping
    public Result getAllEmpWithSalary(@RequestParam(defaultValue = "1", required = true) Integer page,
                                      @RequestParam(defaultValue = "10", required = true) Integer size) {

        ResultPage resultPage = accountService.getAllEmpWithSalary(page, size);
        return Result.SUCCESS(resultPage);
    }

    @GetMapping("/salaries")
    public Result getAllSalary() {
        List<Salary> list = salaryService.getAllSalary();
        return Result.SUCCESS(list);
    }

    @PutMapping
    public Result updateSalaryById(Integer eid, Integer sid) {

        Integer result = accountService.updateSalaryById(eid, sid);
        if (result == 1 || result == 2) {
            return Result.SUCCESS("修改成功");
        }
        return Result.FAIL("修改失败");
    }
}
