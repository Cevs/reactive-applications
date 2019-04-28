package com.cevs.reactive.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private long id;
    private String email;
    private String username;
    private String password;
    private String imageName;
    private List<Role> roles;

    public User(String email, String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
        this.imageName = "";
        this.roles = Arrays.asList(new Role(2,"ROLE_USER"));
    }

    public User(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.imageName = user.getImageName();
        this.roles = user.getRoles();
    }
}
