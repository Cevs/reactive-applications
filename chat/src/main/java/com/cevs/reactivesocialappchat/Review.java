package com.cevs.reactivesocialappchat;

import lombok.Data;

@Data
public class Review {
    private long userId;
    private long productId;
    private String comment;
    private String date;
}
