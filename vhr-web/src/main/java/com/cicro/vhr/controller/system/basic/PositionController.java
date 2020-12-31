package com.cicro.vhr.controller.system.basic;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.entity.ResultCode;
import com.cicro.vhr.entity.ResultPage;
import com.cicro.vhr.model.Position;
import com.cicro.vhr.service.PositionService;
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
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping
    public Result queryPosition(Integer startPage, Integer pageSize) {
        ResultPage resultPage = positionService.queryPosition(startPage, pageSize);
        Result result = new Result(ResultCode.SUCCESS);
        result.setMessage("查询成功");
        result.setData(resultPage);
        return result;
    }

    @PostMapping
    public Result addPosition(@RequestBody Position position) {
        return positionService.addPosition(position);
    }

    @PutMapping
    public Result updatePosition(@RequestBody Position position) {
        return positionService.updatePosition(position);
    }

    @DeleteMapping
    public Result delPositionById(@RequestParam("ids") Integer[] ids) {
        return positionService.delPositionByIds(ids);
    }

}
