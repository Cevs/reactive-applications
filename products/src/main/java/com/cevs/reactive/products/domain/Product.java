package com.cevs.reactive.products.domain;

import com.cevs.reactive.products.dto.ProductDto;
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

    @Id
    private long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String locationName;
    private int quantity;
    private boolean available;
    private int baseDiscount;
    private String imageName;

    public Product(ProductDto productDto){
        this.id = productDto.getId();
        this.name = productDto.getName();
        this.description = productDto.getDescription();
        this.price = productDto.getPrice();
        this.category = productDto.getCategory();
        this.locationName = productDto.getLocationName();
        this.quantity = productDto.getQuantity();
        this.available = productDto.isAvailable();
        this.baseDiscount = productDto.getBaseDiscount();
        this.imageName = "image"+id+".jpg";
    }
}
