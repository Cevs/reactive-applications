package com.cevs.reactivesocialapp.products;

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

    @Id private String id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageName;
}
