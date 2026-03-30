package org.fitness.user_service.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    @Getter
    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    // ── Access Token ──────────────────────────────────────────────

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = userDetails.getAuthorities()
                .stream()
                .collect(Collectors.toMap(
                        GrantedAuthority::getAuthority,
                        auth -> true
                ));


        return buildToken(userDetails.getUsername(), claims, expirationMs);
    }

    // ── Refresh Token ─────────────────────────────────────────────

    public String generateRefreshToken() {
        // Refresh token is just a random opaque string — not a JWT
        // It has no payload to steal, making it safer
        return UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
    }

    // ── Shared ────────────────────────────────────────────────────

    private String buildToken(String subject, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getExpirationDateFromToken(String token) {
        return extractClaims(token).getExpiration().toString();
    }
}
