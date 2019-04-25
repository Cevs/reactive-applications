package com.cevs.reactive.shop.dto;

import com.esotericsoftware.kryo.NotNull;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String rePassword;
}
