package com.cevs.reactive.shop.services;

import com.cevs.reactive.shop.domain.Category;
import reactor.core.publisher.Flux;

public interface CategoryService {
    Flux<Category> getAllCategories();
}
