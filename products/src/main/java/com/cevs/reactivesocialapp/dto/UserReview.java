package com.cevs.reactivesocialapp.dto;

import com.cevs.reactivesocialapp.User;
import com.cevs.reactivesocialapp.products.Review;
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
