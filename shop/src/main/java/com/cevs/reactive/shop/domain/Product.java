package com.cevs.reactive.shop.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Product {

    @Id private long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private int quantity;
    private boolean available;
    private int baseDiscount;
    private String imageName;
}
