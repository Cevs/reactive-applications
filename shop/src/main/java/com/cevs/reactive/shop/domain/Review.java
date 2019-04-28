package com.cevs.reactive.shop.domain;

import com.cevs.reactive.shop.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private long userId;
    private long productId;
    private String comment;
    private String date;

    public Review(long userId, ReviewDto reviewDto){
        this.userId = userId;
        this.productId = reviewDto.getProductId();
        this.comment = reviewDto.getComment();
        this.date = reviewDto.getDate();
    }
}
