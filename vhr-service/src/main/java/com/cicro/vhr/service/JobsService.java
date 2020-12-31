package com.cicro.vhr.service;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.entity.ResultPage;
import com.cicro.vhr.mapper.JobLevelMapper;
import com.cicro.vhr.model.JobLevel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JobsService {

    @Autowired
    private JobLevelMapper jobLevelMapper;

    public ResultPage queryJobLevel(Integer startPage, Integer pageSize) {

        if (null == startPage || startPage <= 0) {
            startPage = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 3;
        }
        PageHelper.startPage(startPage, pageSize);
        List<JobLevel> jobLevels = jobLevelMapper.queryJobs();
        PageInfo page = new PageInfo(jobLevels);
        ResultPage resultPage = ResultPage.builder().currPage(startPage)
            .pageSize(pageSize)
            .list(page.getList())
            .totalCount(page.getTotal())
            .pageTotal(page.getPages())
            .build();
        return resultPage;
    }

    public Result addJobLevel(JobLevel jobLevel) {
        jobLevel.setEnabled(true);
        jobLevel.setCreateDate(new Date());
        int insert = jobLevelMapper.insert(jobLevel);
        Result result = null;
        if (insert > 0) {
            result  = new Result(ResultCode.SUCCESS);
            result.setMessage("添加成功");
            return result;
        }
        result  = new Result(ResultCode.FAIL);
        return result;
    }

    public Result updateJobLevel(JobLevel jobLevel) {
        int i = jobLevelMapper.updateByPrimaryKeySelective(jobLevel);
        Result result = null;
        if (i > 0) {
            result  = new Result(ResultCode.SUCCESS);
            result.setMessage("修改成功");
            return result;
        }
        result  = new Result(ResultCode.FAIL);
        return result;
    }


    public Result delJobLevelByIds(Integer[] ids) {
        int i = jobLevelMapper.delJobLevelByIds(ids);
        Result result = null;
        if (i > 0) {
            result  = new Result(ResultCode.SUCCESS);
            result.setMessage("删除成功");
            return result;
        }
        result  = new Result(ResultCode.FAIL);
        return result;
    }
}
