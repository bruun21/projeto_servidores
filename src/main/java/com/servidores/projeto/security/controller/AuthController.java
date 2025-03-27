package com.servidores.projeto.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servidores.projeto.security.dto.AuthRequest;
import com.servidores.projeto.security.dto.AuthResponse;
import com.servidores.projeto.security.dto.RefreshTokenRequest;
import com.servidores.projeto.security.dto.RoleDto;
import com.servidores.projeto.security.dto.UserRequest;
import com.servidores.projeto.security.exception.ApiRequestException;
import com.servidores.projeto.security.exception.AuthException;
import com.servidores.projeto.security.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) throws AuthException {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao registrar usuário");
        }
    }

    @PostMapping("/register-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // [[5]]
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(authService.createRole(roleDto));
        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}
