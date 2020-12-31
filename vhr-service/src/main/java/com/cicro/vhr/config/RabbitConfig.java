package com.cicro.vhr.config;

import com.cicro.vhr.entity.MailConstants;
import com.cicro.vhr.service.MailSendLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitConfig {

    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MailSendLogService mailSendLogService;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        //消息发送到broker时的回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData data, boolean ack, String cause) {
                String msgId = data.getId(); //拿到消息的唯一ID
                if (ack) {
                    mailSendLogService.updateStatus(msgId, 1); //修改数据库中的状态,消息投递成功
                    log.info("{}:消息发送成功", msgId);
                } else {
                    log.info("{}消息发送失败", msgId);
                }
            }
        });

        //消息在broker中从exchange往queue中发送失败的回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
                                        String routingKey) {
                log.info("{}消息发送失败");
            }
        });
        return rabbitTemplate;
    }

    @Bean
    Queue mailQueue() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME, true);
    }

    @Bean
    DirectExchange mailExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME, true, false);
    }

    //将队列和交换机进行绑定
    @Bean
    Binding mailBind() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }
}
