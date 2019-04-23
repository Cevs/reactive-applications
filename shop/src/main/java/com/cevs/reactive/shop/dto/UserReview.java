package com.cevs.reactive.shop.dto;

import com.cevs.reactive.shop.domain.User;
import com.cevs.reactive.shop.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReview {
    private User user;
    private Review review;
}
