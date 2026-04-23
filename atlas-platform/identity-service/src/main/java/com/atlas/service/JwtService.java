package com.atlas.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
     public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

     public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
     }

     private String createToken(Map<String, Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
     }

     private Key getKey(){
        byte[] keyBytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
     }

     public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
     }
}