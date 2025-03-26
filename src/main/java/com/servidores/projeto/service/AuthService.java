package com.servidores.projeto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.servidores.projeto.dto.AuthRequest;
import com.servidores.projeto.dto.AuthResponse;
import com.servidores.projeto.dto.RefreshTokenRequest;
import com.servidores.projeto.exception.TokenRefreshException;
import com.servidores.projeto.mapper.UserMapper;
import com.servidores.projeto.model.User;
import com.servidores.projeto.repository.UserRepository;

import jakarta.security.auth.message.AuthException;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, JwtService jwtService,
            AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    public AuthResponse authenticate(AuthRequest request) throws AuthException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email().toLowerCase(), // Normaliza email
                            request.password()));

            User user = (User) authentication.getPrincipal();
            if (user == null) {
                throw new AuthException("Falha ao obter detalhes do usuário autenticado");
            }

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            log.info("Login realizado para o usuário: {}", user.getEmail());

            return new AuthResponse(
                    accessToken,
                    refreshToken,
                    "Bearer",
                    userMapper.toResponse(user));

        } catch (BadCredentialsException e) {
            log.warn("Tentativa de login com credenciais inválidas: {}", request.email());
            throw new AuthException("Combinação email/senha incorreta", e);
        } catch (DisabledException e) {
            log.warn("Tentativa de login em conta desativada: {}", request.email());
            throw new AuthException("Conta desativada", e);
        } catch (Exception e) {
            log.error("Erro inesperado durante autenticação", e);
            throw new AuthException("Erro interno durante autenticação");
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtService.isValidRefreshToken(refreshToken)) {
            throw new TokenRefreshException("Refresh token inválido ou expirado");
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String newAccessToken = jwtService.generateToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Bearer",
                userMapper.toResponse(user));
    }
}