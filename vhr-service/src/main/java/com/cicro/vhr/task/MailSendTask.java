package com.cicro.vhr.task;

import com.cicro.vhr.entity.MailConstants;
import com.cicro.vhr.model.Employee;
import com.cicro.vhr.model.MailSendLog;
import com.cicro.vhr.service.EmpService;
import com.cicro.vhr.service.MailSendLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class MailSendTask {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmpService empService;

    @Autowired
    private MailSendLogService sendLogService;

    @Scheduled(cron = "0/10 * * * * ? ")
    public void mailSend() {

        //查询出所有超时后还未发送成功的消息
        //select * from mail_send_log where status=0 and tryTime &lt; sysdate();
        List<MailSendLog> MailSendLogs = sendLogService.getEmpByStatus();

        MailSendLogs.forEach(mail -> {
            if (mail.getCount() >= 3) {
                //重试次数已到,直接判定发送失败
                // update mail_send_log set status=#{status} where msgId=#{msgId}
                sendLogService.updateStatus(mail.getMsgId(), 2);
            } else {
                log.info("消息[{}]开始重试[{}]次", mail.getMsgId(), mail.getCount());
                //增加发送重试次数
                // update mail_send_log set count=count+1,updateTime=#{date} where msgId=#{msgId}
                sendLogService.updateCount(mail.getMsgId(), new Date());
                Employee emp = empService.getEmpById(mail.getEmpId());
                rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME,
                    emp, new CorrelationData(mail.getMsgId()));
            }
        });

    }
}
