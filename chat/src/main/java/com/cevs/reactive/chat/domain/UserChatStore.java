package com.cevs.reactive.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "UserChatStore")
@NoArgsConstructor
@AllArgsConstructor
public class UserChatStore {
    @Id
    private String owner;
    private List<Chat> chats;

    public UserChatStore(String username){
        this.owner = username;
        this.chats = new ArrayList<>();
    }
}
