package com.cevs.reactive.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chat")
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    private String receiver;
    private String sender;
    private String message;
}
