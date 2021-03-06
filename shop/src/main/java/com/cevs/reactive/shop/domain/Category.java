package com.cevs.reactive.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "category")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private long categoryId;
    private String name;
}
