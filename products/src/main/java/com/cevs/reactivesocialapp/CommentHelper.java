package com.cevs.reactivesocialapp;

import com.cevs.reactivesocialapp.domain.Review;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CommentHelper {

    private Logger log = LoggerFactory.getLogger(CommentHelper.class);
    private final RestTemplate restTemplate;

    public CommentHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "defaultComments")
    public List<Review> getComments(long productId){
        List<Review> reviewList = new ArrayList<>();
        reviewList = restTemplate.exchange(
                "http://COMMENTS/comments/{productId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {},
                productId).getBody();
        log.info("REVIEW_LIST: "+reviewList.get(0));
        return reviewList;
    }

    public List<Review> defaultComments(long produceId){
        log.info("Return empty");
        return Collections.emptyList();
    }
}
