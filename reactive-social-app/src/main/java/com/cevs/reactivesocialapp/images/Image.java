package com.cevs.reactivesocialapp.images;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="images")
@NoArgsConstructor
@Getter
@Setter
public class Image {

    @Id private String id;
    private String name;

    public Image(String id, String name){
        this.id = id;
        this.name = name;
    }
}
