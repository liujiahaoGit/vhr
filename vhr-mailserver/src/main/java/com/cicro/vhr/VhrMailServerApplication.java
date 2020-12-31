package com.cicro.vhr;

import com.cicro.vhr.entity.MailConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VhrMailServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VhrMailServerApplication.class, args);
    }

    /*配置queue队列*/
    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

}
