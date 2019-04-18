package com.cevs.reactivesocialapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "category")
@AllArgsConstructor
public class Category {
    @Id
    private long id;
    private String name;
}
