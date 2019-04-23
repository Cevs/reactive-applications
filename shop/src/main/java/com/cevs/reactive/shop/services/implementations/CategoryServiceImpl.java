package com.cevs.reactivesocialapp.services.implementations;

import com.cevs.reactivesocialapp.domain.Category;
import com.cevs.reactivesocialapp.repositories.CategoryRepository;
import com.cevs.reactivesocialapp.services.CategoryService;
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
