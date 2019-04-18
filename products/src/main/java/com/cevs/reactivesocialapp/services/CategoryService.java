package com.cevs.reactivesocialapp.services;

import com.cevs.reactivesocialapp.domain.Category;
import reactor.core.publisher.Flux;

public interface CategoryService {
    Flux<Category> getAllCategories();
}
