package org.example.filmbuffsforum.auth.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.filmbuffsforum.auth.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    private final SecretKey key;
    private final Duration tokenExpiration;

    public JwtUtils(
            @Value("${app.jwt.secret}") String jwtSecret,
            @Value("${app.jwt.tokenExpiration}") Duration tokenExpiration
    ) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.tokenExpiration = tokenExpiration;
    }

    public String generateToken(AppUserDetails userDetails) {
        return generateTokenFromUsername(userDetails.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenExpiration.toMillis());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        }
        catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        }
        catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
