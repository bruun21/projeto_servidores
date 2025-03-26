package com.servidores.projeto.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.servidores.projeto.config.JwtConfig;
import com.servidores.projeto.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;


    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, jwtConfig.getExpirationMs(), "ACCESS");
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, jwtConfig.getRefreshExpirationMs(), "REFRESH");
    }

    private String buildToken(UserDetails userDetails, long expirationMs, String tokenType) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("token_type", tokenType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public boolean isValidRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.get("token_type", String.class).equals("REFRESH") &&
                   !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean canTokenBeRefreshed(String token) {
        final Date expiration = extractExpiration(token);
        final long timeLeft = expiration.getTime() - System.currentTimeMillis();
        return timeLeft > 0 && timeLeft < jwtConfig.getRefreshWindowMs();
    }

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationMs()))
                .signWith(secretKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
