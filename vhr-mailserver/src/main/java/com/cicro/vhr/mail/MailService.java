package com.cicro.vhr.mail;

import com.cicro.vhr.entity.MailConstants;
import com.cicro.vhr.model.Employee;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class MailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MailProperties mailProperties;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void sendMail(Message message, Channel channel) throws IOException {
        Employee employee = (Employee) message.getPayload(); //监听接受到的消息内容
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG); //消息唯一标记
        String msgId = headers.get("spring_returned_message_correlation").toString();  //这里的消息ID即为生产端所生成的消息UUID

        //当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
        if (redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)) {
            log.info("[{}]消息已消费过", msgId);
            channel.basicAck(tag, false); //手动确认消息已消费
            return;
        }
        log.info("收到消息{}", employee.toString());

        //发送邮件的相关业务逻辑
        Context context = new Context();
        context.setVariable("name", employee.getName());
        context.setVariable("gender", employee.getGender());
        context.setVariable("posName", employee.getPosition().getName());
        context.setVariable("deptName", employee.getDepartment().getName());
        context.setVariable("jobLevelName", employee.getJobLevel().getName());
        String mail = templateEngine.process("mail", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("入职邮件");
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(employee.getEmail());
            helper.setSentDate(new Date());
            helper.setText(mail, true); //是否为html
            javaMailSender.send(mimeMessage);

            channel.basicAck(tag, false); //手动确认消息已消费
            log.info("[{}]消费消息",msgId);
            redisTemplate.opsForHash().put("mail_log",msgId,msgId); //存入redis
        } catch (MessagingException e) {

            channel.basicNack(tag,false,true); //异常时消息退回队列,等待下次重新消费
            log.info("给{}的邮件发送失败", employee.getName());
            e.printStackTrace();
        }

    }

}
