package com.cevs.reactive.shop.domain.initDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String receiver;
    private String sender;
    private String text;
}
