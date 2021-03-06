package com.cevs.reactive.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    private String receiver;
    private List<Message> messages;

    public Chat(String receiver){
        this.receiver = receiver;
        this.messages = new ArrayList<>();
    }
}
