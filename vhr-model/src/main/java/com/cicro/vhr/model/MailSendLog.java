package com.cicro.vhr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MailSendLog {

    private String msgId;
    private Integer empId;

    //0:消息投递中  1:投递成功  2:投递失败
    private Integer status;
    private String routeKey;
    private String exchange;
    private Integer count;
    private Date tryTime;
    private Date createTime;
    private Date updateTime;

}
