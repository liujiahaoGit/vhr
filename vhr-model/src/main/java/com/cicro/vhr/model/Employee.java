package com.cicro.vhr.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Employee implements Serializable {

    // name :Excel标题字段名称 width: 宽  height: 高
    @Excel(name = "员工编号", width = 10, height = 30)
    private Integer id;

    @Excel(name = "员工姓名", width = 20, height = 30)
    private String name;

    @Excel(name = "性别", width = 10, height = 30)
    private String gender;


    @Excel(name = "出生年月", width = 10, height = 30, format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")  //以JSON返回给前端时时间要进行格式化处理
    private Date birthday;

    @Excel(name = "身份证号", width = 50, height = 30)
    private String idCard;

    @Excel(name = "婚姻状况", width = 10, height = 30)
    private String wedlock;


    private Integer nationId;
    @Excel(name = "民族", width = 10, height = 30)
    private String nationName;

    @Excel(name = "籍贯", width = 10, height = 30)
    private String nativePlace;


    private Integer politicId;
    @Excel(name = "政治面貌", width = 15, height = 30)
    private String politic;

    @Excel(name = "邮箱", width = 20, height = 30)
    private String email;

    @Excel(name = "电话号码", width = 20, height = 30)
    private String phone;

    @Excel(name = "联系地址", width = 10, height = 30)
    private String address;


    private Integer departmentId;
    @Excel(name = "所属部门", width = 10, height = 30)
    private String departmentName;


    private Integer jobLevelId;
    @Excel(name = "职称", width = 10, height = 30)
    private String jobLevelName;


    private Integer posId;
    @Excel(name = "职位", width = 10, height = 30)
    private String pos;

    @Excel(name = "聘用形式", width = 10, height = 30)
    private String engageForm;

    @Excel(name = "最高学历", width = 10, height = 30)
    private String tiptopDegree;

    @Excel(name = "所属专业", width = 10, height = 30)
    private String specialty;

    @Excel(name = "毕业院校", width = 10, height = 30)
    private String school;

    @Excel(name = "入职日期", width = 10, height = 30, format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginDate;

    @Excel(name = "在职状态", width = 10, height = 30)
    private String workState;

    @Excel(name = "工号", width = 10, height = 30)
    private String workId;

    @Excel(name = "合同期限", width = 10, height = 30)
    private Double contractTerm;

    @Excel(name = "转正日期", width = 10, height = 30, format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date conversionTime;

    @Excel(name = "离职日期", width = 10, height = 30, format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date notworkDate;

    @Excel(name = "合同起始日期", width = 10, height = 30, format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date beginContract;

    @Excel(name = "合同终止日期", width = 10, height = 30, format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endContract;

    @Excel(name = "工龄", width = 10, height = 30)
    private Integer workAge;

    private Nation nation;

    private Politicsstatus politicsstatus;

    private Department department;

    private JobLevel jobLevel;

    private Position position;

    private Salary salary;
}