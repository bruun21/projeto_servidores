package com.servidores.projeto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {
    private final String token;

    public TokenRefreshException(String token, String message) {
        super(String.format("Falha no refresh token [%s]: %s", token, message));
        this.token = token;
    }

    public TokenRefreshException(String message) {
        super(message);
        this.token = null;
    }

    public String getToken() {
        return token;
    }
}