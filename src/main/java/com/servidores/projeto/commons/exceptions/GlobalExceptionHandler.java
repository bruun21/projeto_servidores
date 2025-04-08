package com.servidores.projeto.commons.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.servidores.projeto.security.exception.AuthException;
import com.servidores.projeto.security.exception.TokenRefreshException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        logger.warn("Erro de autenticação: {}", ex.getMessage(), ex); // Loga a exceção completa
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getMessage(), "AUTH_ERROR"));
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handleTokenRefreshException(TokenRefreshException ex) {
        logger.warn("Erro ao atualizar token: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getMessage(), "TOKEN_REFRESH_ERROR"));
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
        logger.error("Propriedade inválida acessada: {}", ex.getPropertyName(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "Propriedade inválida: " + ex.getPropertyName(),
                        "INVALID_PROPERTY"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Erro interno não tratado", ex); // Loga a stacktrace completa
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "Ocorreu um erro interno no servidor",
                        "INTERNAL_ERROR"));
    }

    public record ErrorResponse(String message, String errorCode) {
    }

    @ExceptionHandler(ModelNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleModelNaoEncontradaException(ModelNaoEncontradaException ex) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), "RESOURCE_NOT_FOUND"));
    }
}
