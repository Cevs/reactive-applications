package com.cevs.reactive.shop.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http.authorizeExchange()
                .pathMatchers("/registration*", "/resources/**", "/templates/**", "/login*","/user/exist",
                        "/error", "/successRegistration*", "/upload-dir/**", "/", "/products/**", "/css/**", "/js/**", "/images/**")
                .permitAll()
                .anyExchange().authenticated()
                .and().formLogin().loginPage("../login")
                .and().csrf().disable()
                .build();
    }
}
