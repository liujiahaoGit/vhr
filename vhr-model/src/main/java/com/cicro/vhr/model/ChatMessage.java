package com.cicro.vhr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessage {

    private String from;

    private String to;

    private String content;

    private Date date;

    private String nickName;
}
