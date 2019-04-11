package com.cevs.reactivesocialapp.products;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class CommentHelper {

    private final RestTemplate restTemplate;

    public CommentHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "defaultComments")
    public List<Comment> getComments(Product product){
        return restTemplate.exchange(
                "http://COMMENTS/comments/{productId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Comment>>() {},
                product.getId()).getBody();
    }

    public List<Comment> defaultComments(Product image){
        return Collections.emptyList();
    }
}
