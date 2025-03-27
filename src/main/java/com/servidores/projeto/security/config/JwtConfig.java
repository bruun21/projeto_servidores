package com.servidores.projeto.security.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtConfig {
    private String secret;
    private long expirationMs;
    private long refreshWindowMs;
    private long refreshExpirationMs;

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public long getExpirationMs() { return expirationMs; }
    public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }

    public long getRefreshWindowMs() { return refreshWindowMs; }
    public void setRefreshWindowMs(long refreshWindowMs) { this.refreshWindowMs = refreshWindowMs; }

    public long getRefreshExpirationMs() { return refreshExpirationMs; }
    public void setRefreshExpirationMs(long refreshExpirationMs) { this.refreshExpirationMs = refreshExpirationMs; }
}