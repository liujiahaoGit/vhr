package com.cicro.vhr.controller.salary;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.model.Salary;
import com.cicro.vhr.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @GetMapping
    public Result getAllSalary() {
        List<Salary> allSalary = salaryService.getAllSalary();
        return Result.SUCCESS(allSalary);
    }

    @PostMapping
    public Result saveSalary(@RequestBody Salary salary) {
        Integer result = salaryService.saveSalary(salary);
        if (result == 1) {
            return Result.SUCCESS("添加成功");
        }
        return Result.FAIL("添加失败");
    }

    @DeleteMapping("/{id}")
    public Result deleteSalaryById(@PathVariable Integer id) {
        Integer result = salaryService.deleteSalaryById(id);
        if (result == 1) {
            return Result.SUCCESS("删除成功");
        }
        return Result.FAIL("删除失败");
    }

    @PutMapping
    public Result updateSalary(@RequestBody Salary salary) {
        Integer result = salaryService.updateSalary(salary);
        if (result == 1) {
            return Result.SUCCESS("修改成功");
        }
        return Result.FAIL("修改失败");
    }

    @DeleteMapping
    public Result deleteSalaryByIds(Integer[] ids) {
        Integer result = salaryService.deleteSalaryByIds(ids);
        if (result == ids.length) {
            return Result.SUCCESS("删除成功");
        }
        return Result.FAIL("删除失败");
    }
}
