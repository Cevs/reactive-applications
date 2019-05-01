package com.cevs.reactive.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "ShoppingCart")
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @Id
    private long userId;
    private List<Product> products;

    public ShoppingCart(long userId){
        this.userId = userId;
        this.products = new ArrayList<>();
    }
}
