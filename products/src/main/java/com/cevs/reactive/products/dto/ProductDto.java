package com.cevs.reactive.products.dto;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

import javax.validation.constraints.Null;

@Data
public class ProductDto {
    @Null
    private long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private int quantity;
    private boolean available;
    private int baseDiscount;
    private FilePart image;
}
