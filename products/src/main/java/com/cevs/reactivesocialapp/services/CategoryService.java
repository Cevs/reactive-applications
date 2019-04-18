package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.domain.Category;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    Flux<Category> getAllCategories();
    Mono<Category> getCategoryById(long categoryId);
}
