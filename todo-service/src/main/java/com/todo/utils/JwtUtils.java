package com.todo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    // ⚠️ Use the exact same secret as in user-service
    private final String jwtSecret = "zZrP3l7Vr3k+H6ZQq+8a0bE9Lhz7VN/fBFfqQ5C3XZeF3+7YFmmkgBgXFGx2KujdRmYt0wTeZ5TGSjzpaKl7Tw==";

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

