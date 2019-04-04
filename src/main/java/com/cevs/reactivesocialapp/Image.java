package com.cevs.reactivesocialapp;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Getter
@Setter
public class Image {

    private String id;
    private String name;

    public Image(String id, String name){
        this.id = id;
        this.name = name;
    }
}
