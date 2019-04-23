package com.cevs.reactive.shop.services.implementations;

import com.cevs.reactive.shop.domain.Category;
import com.cevs.reactive.shop.repositories.CategoryRepository;
import com.cevs.reactive.shop.services.CategoryService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Flux<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
