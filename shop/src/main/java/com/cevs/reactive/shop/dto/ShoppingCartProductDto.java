package com.cevs.reactive.shop.dto;

import com.cevs.reactive.shop.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartProductDto {
    private Product product;
    private Long quantity;
}
