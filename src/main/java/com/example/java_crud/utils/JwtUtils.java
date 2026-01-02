package com.example.java_crud.utils;

import com.example.java_crud.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    private final Key signingKey;
    private final long expirationMillis = 7 * 24 * 60 * 60 * 1000;
    private final long refreshTokenExpirationMillis = 7 * 24 * 60 * 60 * 1000;

    public JwtUtils(@Value("${jwt.secret}") String secretKey) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("Please set jwt.secret in application.properties.");
        }
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMillis))
                .addClaims(Map.of(
                        "Id", String.valueOf(user.getId()),
                        "Name", user.getName(),
                        "Email", user.getEmail(),
                        "Role", user.getRole()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // generate the refresh tokne here
    public String generateRefreshToken(User user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpirationMillis))
                .addClaims(Map.of(
                        "Id", String.valueOf(user.getId()),
                        "Name", user.getName(),
                        "Email", user.getEmail(),
                        "Role", user.getRole()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateAndGetClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public User extractUser(String token) {
        Claims claims = validateAndGetClaims(token);
        User user = new User();

        Object idClaim = claims.get("Id");
        if (idClaim != null) {
            user.setId(Long.parseLong(idClaim.toString()));
        }

        user.setName(claims.getOrDefault("Name", "").toString());
        user.setEmail(claims.getOrDefault("Email", claims.getSubject()).toString()); // fallback to subject
        user.setRole(claims.getOrDefault("Role", "").toString());

        return user;
    }

}
