package com.cevs.reactive.chat.domain;

import lombok.Data;

@Data
public class Review {
    private long userId;
    private long productId;
    private String comment;
    private String date;
}
