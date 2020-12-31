package com.cicro.vhr.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class ResultPage {
    //当前页
    private Integer currPage;
    //每页显示条数
    private Integer pageSize;

    //总条数
    private Long totalCount;
    //总页数
    private Integer pageTotal;


    //结果集
    private List<?> list;

}
