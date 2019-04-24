package com.cevs.reactive.shop.dto;

import lombok.*;
import org.springframework.http.codec.multipart.FilePart;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto{
    private String name;
    private String description;
    private double price;
    private String category;
    private String locationName;
    private int quantity;
    private boolean available;
    private int baseDiscount;
    private FilePart image;
}
