package com.cicro.vhr.service;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.entity.ResultPage;
import com.cicro.vhr.mapper.PositionMapper;
import com.cicro.vhr.model.Position;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PositionService {

    @Autowired
    private PositionMapper positionMapper;

    public ResultPage queryPosition(Integer startPage, Integer pageSize) {

        if (null == startPage || startPage <= 0) {
            startPage = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 3;
        }
        PageHelper.startPage(startPage, pageSize);
        List<Position> positions = positionMapper.queryPosition();
        PageInfo page = new PageInfo(positions);
        ResultPage resultPage = ResultPage.builder().currPage(startPage)
            .pageSize(pageSize)
            .list(page.getList())
            .totalCount(page.getTotal())
            .pageTotal(page.getPages())
            .build();
        return resultPage;
    }

    public Result addPosition(Position position) {
        position.setEnabled(true);
        position.setCreateDate(new Date());
        int insert = positionMapper.insert(position);
        Result result = null;
        if (insert > 0) {
            result  = new Result(ResultCode.SUCCESS);
            result.setMessage("添加成功");
            return result;
        }
        result  = new Result(ResultCode.FAIL);
        return result;
    }

    public Result updatePosition(Position position) {
        int i = positionMapper.updateByPrimaryKeySelective(position);
        Result result = null;
        if (i > 0) {
            result  = new Result(ResultCode.SUCCESS);
            result.setMessage("修改成功");
            return result;
        }
        result  = new Result(ResultCode.FAIL);
        return result;
    }


    public Result delPositionByIds(Integer[] ids) {
        int i = positionMapper.delPositionByIds(ids);
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
