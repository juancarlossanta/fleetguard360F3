package com.udea.fleetguard360F3.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Configuration
public class JwtConfig {
    @Value("${app.jwt.secret}")
    private String secret;

    @Bean
    public Key jwtKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
