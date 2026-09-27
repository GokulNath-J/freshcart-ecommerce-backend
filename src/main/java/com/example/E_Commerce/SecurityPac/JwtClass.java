package com.example.E_Commerce.SecurityPac;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtClass {


    private final SecretKey Secret_key = Keys.hmacShaKeyFor("mD4%zL!pT8@7rHqU#xCwF9aM3dP2gV1o".getBytes());

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .claims()
                .subject(username)
                .issuer("Gokul")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .add("Role", role)
                .and()
                .signWith(Secret_key, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(Secret_key)
                .build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public boolean verifyToken(String token, String username) {
        String name = extractUsername(token);
        return (name.equals(username) && !isTokenExpried(token));
    }

    public boolean isTokenExpried(String token) {
        JwtParser jwtParser = Jwts.parser().verifyWith(Secret_key).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        Date date = claims.getExpiration();
        return date.before(new Date());
    }
}
