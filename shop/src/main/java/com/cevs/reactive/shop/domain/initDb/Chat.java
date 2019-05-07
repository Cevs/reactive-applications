package com.cevs.reactive.shop.domain.initDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    private String receiver;
    private List<Message> messages;
}
