package com.cevs.reactivesocialapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private int quantity;
    private boolean available;
    private int baseDiscount;
    private FilePart image;
}
