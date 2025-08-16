package com.employee.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final Key secretKey = Keys.hmacShaKeyFor("your-256-bit-secret-your-256-bit-secret".getBytes());

    public String generateToken(Map<String, Object> claims) {
        long expirationMillis = 1000 * 60 * 30; // 30 minutes
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Add this method for token validation
    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date issuedAt = claims.getIssuedAt();
            Date now = new Date();

            // Check if token was issued more than 15 minutes ago
            long fifteenMinutesInMillis = 15 * 60 * 1000;
            if (now.getTime() - issuedAt.getTime() > fifteenMinutesInMillis) {
                throw new RuntimeException("Link expired: Token was issued more than 15 minutes ago.");
            }

            // Optionally: Expiration is already handled by jjwt, it throws ExpiredJwtException if expired

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Link expired: Token has expired.", e);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token.", e);
        }
    }
}
