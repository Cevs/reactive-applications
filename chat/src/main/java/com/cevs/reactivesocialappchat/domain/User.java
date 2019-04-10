package com.cevs.reactivesocialappchat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable {

    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRoles();
    }

    @Id private String id;
    private String username;
    private String password;
    private String roles[];
}
