/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Util;

/**
 *
 * @author USER
 */
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class Ujwt {
    private final byte[] key;
    private final long EXPIRATION_TIME;

    public Ujwt(@Value("${jwt.secret}") String secret, 
                @Value("${jwt.expiration}") long expirationTime) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("SECRET_KEY harus minimal 32 karakter!");
        }
        this.key = secret.getBytes(StandardCharsets.UTF_8);
        this.EXPIRATION_TIME = expirationTime;
    }

    public String generateToken(String username) { 
        String tkn = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS256)
                .compact();  
        return tkn;
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(key))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            System.out.println(e);
            return null; // Token tidak valid atau expired
        }
    }

    public boolean validateToken(String token, String username) { 
        String extractedUsername = extractUsername(token); 
        return extractedUsername != null && extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) { 
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(key))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            return true; // Anggap token expired jika tidak bisa diparsing
        }
    }
}


