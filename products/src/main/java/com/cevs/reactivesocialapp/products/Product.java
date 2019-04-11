package com.cevs.reactivesocialapp.products;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="products")
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id private String id;
    private String name;
    private String description;
    private double price;

    public Product(String id, String name){
        this.id = id;
        this.name = name;
    }
}
