package com.growtivat.auth_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${access.token.secret.key}")
    private String accessTokenSecretKey;

    @Value("${refresh.token.secret.key}")
    private String refreshTokenSecretKey;

    @Value("${access.token.ttl}")
    private Long accessTokenTTL;

    @Value("${refresh.token.ttl}")
    private Long refreshTokenTTL;

    private String getSecretKey(TokenType type) {
        return type == TokenType.ACCESS ? accessTokenSecretKey : refreshTokenSecretKey;
    }

    private long getTokenTTL(TokenType type) {
        return type == TokenType.ACCESS ? accessTokenTTL : refreshTokenTTL;
    }

    public String generateToken(String subject, TokenType type) {
        return Jwts.builder()
                .issuer("growtivat.auth-service")
                .subject(subject)
                .audience().add("growtivat.com")
                .and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + getTokenTTL(type)))
                .signWith(Keys.hmacShaKeyFor(getSecretKey(type).getBytes()))
                .compact();
    }

    public Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(getSecretKey(type).getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token, TokenType type) {
        return extractAllClaims(token, type).getSubject();
    }

    public boolean isTokenExpired(String token, TokenType type) {
        Date expiration = extractAllClaims(token, type).getExpiration();
        return expiration.before(new Date());
    }

    public boolean isValidToken(String token, UserDetails userDetails, TokenType type) {
        String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, type);
    }
}
