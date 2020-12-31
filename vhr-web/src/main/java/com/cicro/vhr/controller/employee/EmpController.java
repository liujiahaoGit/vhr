package com.cicro.vhr.controller.employee;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultPage;
import com.cicro.vhr.model.Department;
import com.cicro.vhr.model.Employee;
import com.cicro.vhr.model.Nation;
import com.cicro.vhr.model.Politicsstatus;
import com.cicro.vhr.service.DeptService;
import com.cicro.vhr.service.EmpService;
import com.cicro.vhr.service.JobsService;
import com.cicro.vhr.service.NationService;
import com.cicro.vhr.service.PoliticsStatusService;
import com.cicro.vhr.service.PositionService;
import com.cicro.vhr.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/employee/basic")
public class EmpController {

    @Autowired
    private EmpService empService;

    @Autowired
    private PoliticsStatusService politicsStatusService;

    @Autowired
    private NationService nationService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private JobsService jobsService;

    @Autowired
    private DeptService deptService;

    @GetMapping
    public Result getAllEmployee(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 Employee employee,
                                 Date[] beginDate) {
        ResultPage resultPage = empService.getAllEmployee(page, size, employee,beginDate);
        return Result.SUCCESS(resultPage);
    }

    @PostMapping
    public Result insertEmp(@RequestBody Employee employee) {

        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        if (endContract.before(beginContract)) {
            return Result.FAIL("合同起始时间不能大于合同终止时间");
        }
        double until = TimeUtils.until(endContract, beginContract);
        employee.setContractTerm(until);

        Integer result = empService.insertEmp(employee);
        if (result == 1) {
            return Result.SUCCESS("添加成功");
        }
        return Result.FAIL("添加失败");
    }

    @GetMapping("/politicsStatus")
    public Result getAllPoliticsStatus() {
        List<Politicsstatus> allPoliticsStatus = politicsStatusService.getAllPoliticsStatus();

        return Result.SUCCESS(allPoliticsStatus);
    }

    @GetMapping("/nation")
    public Result getAllNation() {
        List<Nation> allNation = nationService.getAllNation();

        return Result.SUCCESS(allNation);
    }

    @GetMapping("/position")
    public Result getAllPosition() {
        ResultPage resultPage = positionService.queryPosition(1, 100);

        return Result.SUCCESS(resultPage);
    }

    @GetMapping("/joblevel")
    public Result getAllJoblevel() {
        ResultPage resultPage = jobsService.queryJobLevel(1, 100);

        return Result.SUCCESS(resultPage);
    }

    @GetMapping("/workId")
    public Result getWorkId() {
        String workId = empService.getWorkId();

        return Result.SUCCESS(((Object) workId));
    }

    @GetMapping("/depts")
    public Result getDepts() {
        List<Department> departments = deptService.queryAllDept();

        return Result.SUCCESS(departments);
    }

    @DeleteMapping("/{id}")
    public Result deleteEmpById(@PathVariable Integer id) {
        Integer result = empService.deleteEmpById(id);
        if (result == 1) {
            return Result.SUCCESS("删除成功");
        }
        return Result.FAIL("删除失败");

    }

    @PutMapping
    public Result updateEmpById(@RequestBody Employee employee) {
        Integer result = empService.updateEmpById(employee);
        if (result == 1) {
            return Result.SUCCESS("修改成功");
        }
        return Result.FAIL("修改失败");

    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export() throws IOException {
        return empService.export();

    }

    @PostMapping("/import")
    public Result importEmp(MultipartFile file) throws IOException {
        if (empService.importEmp(file)){
            return Result.SUCCESS("上传成功");
        }
        return Result.FAIL("上传失败");
    }

}
