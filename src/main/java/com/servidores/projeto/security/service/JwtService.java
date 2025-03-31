package com.servidores.projeto.security.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.servidores.projeto.security.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(30) // Tolerância de 30 segundos para diferença de horário
                .build();
    }

    // Gera token de acesso (tempo de vida maior)
    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, jwtConfig.getAccessExpirationMs(), "ACCESS");
    }

    // Gera refresh token (tempo de vida maior que o access token)
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, jwtConfig.getRefreshTokenExpirationMs(), "REFRESH");
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
            return "REFRESH".equals(claims.get("token_type", String.class)) &&
                    !isTokenExpired(token);
        } catch (ExpiredJwtException ex) {
            return false; // Refresh token expirado
        } catch (Exception e) {
            return false; // Token inválido
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }
}