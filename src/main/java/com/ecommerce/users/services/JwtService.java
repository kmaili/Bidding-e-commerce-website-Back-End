package com.ecommerce.users.services;

import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Claims;

public interface JwtService {
    Long extractUserId(String token);
    Date extractExpiration(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(Long userId);
    Boolean validateToken(String token, Long userId);

}
