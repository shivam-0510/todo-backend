package com.todo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    // ⚠️ Use the exact same secret as in user-service
    private final String jwtSecret = System.getenv("JWT_SECRET") != null 
        ? System.getenv("JWT_SECRET") 
        : System.getProperty("jwt.secret", "CHANGE_THIS_SECRET_IN_PRODUCTION_USE_ENVIRONMENT_VARIABLE");

    /**
     * Validate the JWT token.
     * Returns true if valid, otherwise false.
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("Invalid or expired JWT token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extract username (subject) from the JWT token.
     */
    public String getUserNameFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}

