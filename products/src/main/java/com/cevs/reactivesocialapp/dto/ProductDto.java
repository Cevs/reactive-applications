package com.cevs.reactivesocialapp.dto;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

@Data
public class ProductDto {
    private String name;
    private String description;
    private double price;
    private String category;
    private FilePart image;
}
