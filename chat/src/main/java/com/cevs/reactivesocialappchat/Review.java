package com.cevs.reactivesocialappchat;

import lombok.Data;

@Data
public class Review {
    private String userId;
    private String productId;
    private String comment;
    private String date;
}
