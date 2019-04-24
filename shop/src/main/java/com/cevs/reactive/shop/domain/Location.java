package com.cevs.reactive.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "location")
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    private int id;
    private String country;
}
