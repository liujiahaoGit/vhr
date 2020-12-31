package com.cicro.vhr.controller.chat;

import com.cicro.vhr.entity.Result;
import com.cicro.vhr.model.Hr;
import com.cicro.vhr.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/hr/chat")
@RestController
public class WeChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public Result getAllHrIncludeCurrent() {
        List<Hr> list = chatService.getAllHrIncludeCurrent();
        return Result.SUCCESS(list);
    }
}
