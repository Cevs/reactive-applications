package com.cevs.reactive.shop.dto;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

@Data
public class ProfileDto {
    private String username;
    private String password;
    private String email;
    private FilePart profileImage;
}
