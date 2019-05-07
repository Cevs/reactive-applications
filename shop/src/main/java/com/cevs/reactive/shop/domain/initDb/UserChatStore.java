package com.cevs.reactive.shop.domain.initDb;

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

    public UserChatStore(String owner){
        this.owner = owner;
        this.chats = new ArrayList<Chat>();
    }
}
