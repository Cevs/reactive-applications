package com.cevs.reactive.comments.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Review {
    private long userId;
    private long productId;
    private String comment;
    private String date;
}
