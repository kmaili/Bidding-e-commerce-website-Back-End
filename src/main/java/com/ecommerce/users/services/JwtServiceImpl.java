package com.ecommerce.users.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {


    private String signKey = "8c1d1e34c7ec9e97b84b4e6cdb84a97836b2a7055574f6533c02c320d2c05205"; // hard coded (must be stored in env variable for production)


    @Override
    public Long extractUserId(String token) {
        return  Long.parseLong(extractClaim(token, Claims::getSubject));
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(signKey).parseClaimsJws(token).getBody();
    }

    @Override
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userId);
    }

    @Override
    public Boolean validateToken(String token, Long userId) {
        Long tokenUserId = extractUserId(token);
        return (userId.equals(tokenUserId) && ! isTokenExpired(token));
    }

    private String createToken(Map<String, Object> claims, Long subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(subject))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, signKey)
                .compact();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
