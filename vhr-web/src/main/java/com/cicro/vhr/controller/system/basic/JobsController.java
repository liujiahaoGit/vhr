package com.cicro.vhr.controller.system.basic;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.entity.ResultPage;
import com.cicro.vhr.model.JobLevel;
import com.cicro.vhr.service.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/basic/jobs")
public class JobsController {

    @Autowired
    private JobsService jobsService;

    @GetMapping
    public Result queryJobs(Integer startPage, Integer pageSize) {
        ResultPage resultPage = jobsService.queryJobLevel(startPage, pageSize);
        Result result = new Result(ResultCode.SUCCESS);
        result.setMessage("查询成功");
        result.setData(resultPage);
        return result;
    }

    @PostMapping
    public Result addJobs(@RequestBody JobLevel jobLevel) {
        return jobsService.addJobLevel(jobLevel);
    }

    @PutMapping
    public Result updateJobs(@RequestBody JobLevel jobLevel) {
        return jobsService.updateJobLevel(jobLevel);
    }

    @DeleteMapping
    public Result delJobsById(@RequestParam("ids") Integer[] ids) {
        return jobsService.delJobLevelByIds(ids);
    }

}
