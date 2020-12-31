package com.cicro.vhr.controller.chat;

import com.cicro.vhr.model.ChatMessage;
import com.cicro.vhr.model.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class WsController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/ws/chat")
    public void handleMessage(Authentication auth, ChatMessage chatMessage) {
        Hr hr = (Hr) auth.getPrincipal();
        chatMessage.setNickName(hr.getName());
        chatMessage.setFrom(hr.getUsername());
        chatMessage.setDate(new Date());
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getTo(), "/queue/chat", chatMessage);

    }

}


