package com.cevs.reactivesocialapp.dto;

import com.cevs.reactivesocialapp.domain.User;
import com.cevs.reactivesocialapp.domain.Review;
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
