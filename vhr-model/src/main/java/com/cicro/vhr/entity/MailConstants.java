package com.cicro.vhr.entity;

public class MailConstants {

    public static final Integer DELIVERING = 0; //消息投递中
    public static final Integer SUCCESS = 1;  //消息发送成功
    public static final Integer FAIL = 2;  //消息发送失败
    public static final Integer MAX_TRY_COUNT = 3; //最大重试次数
    public static final Integer MSG_TIMEOUT = 1; //消息超时时间
    public static final String MAIL_QUEUE_NAME = "mail_queue"; //消息对列名称
    public static final String MAIL_EXCHANGE_NAME = "mail_exchange"; //消息交换机名称
    public static final String MAIL_ROUTING_KEY_NAME = "mail_routing_key"; //消息ROUTINGKEY

}
