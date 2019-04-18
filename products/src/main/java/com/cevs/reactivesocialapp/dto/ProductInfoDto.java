package com.cevs.reactivesocialapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductInfoDto {
    private long id;
    private String name;
    private String description;
    private double price;
    private String categoryName;
    private int quantity;
    private boolean available;
    private int baseDiscount;
    private String imageName;
}
