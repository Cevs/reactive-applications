package com.cevs.reactive.shop.domain;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.function.Tuple2;

import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection = "ShoppingCart")
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @Id
    private long userId;
    private Map<Long, Long> productIdQuantity;

    public ShoppingCart(long userId){
        this.userId = userId;
        this.productIdQuantity =  new HashMap<>();
    }
}
