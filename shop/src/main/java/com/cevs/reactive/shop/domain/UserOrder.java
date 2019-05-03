package com.cevs.reactive.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "UserOrders")
@AllArgsConstructor
@NoArgsConstructor
public class UserOrder {
    private User user;
    private List<Order> order;
    private LocalDate orderDate;
}
