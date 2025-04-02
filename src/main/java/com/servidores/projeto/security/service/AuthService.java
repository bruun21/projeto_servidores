package com.servidores.projeto.security.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.exceptions.NotFoundException;
import com.servidores.projeto.security.dto.AuthRequest;
import com.servidores.projeto.security.dto.AuthResponse;
import com.servidores.projeto.security.dto.RefreshTokenRequest;
import com.servidores.projeto.security.dto.RoleDto;
import com.servidores.projeto.security.dto.UserRequest;
import com.servidores.projeto.security.exception.ApiRequestException;
import com.servidores.projeto.security.exception.AuthException;
import com.servidores.projeto.security.exception.TokenRefreshException;
import com.servidores.projeto.security.mapper.UserMapper;
import com.servidores.projeto.security.model.Role;
import com.servidores.projeto.security.model.User;
import com.servidores.projeto.security.repository.RoleRepository;
import com.servidores.projeto.security.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, JwtService jwtService,
            AuthenticationManager authenticationManager, UserMapper userMapper, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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

        String newAccessToken = jwtService.generateAccessToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Bearer",
                userMapper.toResponse(user));
    }

    @Transactional
    public AuthResponse register(UserRequest request) throws AuthException {
        String normalizedEmail = request.email().toLowerCase().trim();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new AuthException("Email já registrado");
        }

        Set<Role> roles = request.roleIds().stream()
                .map(id -> roleRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Role id " + id + " não encontrada")))
                .collect(Collectors.toSet());

        User newUser = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .enabled(true)
                .build();

        userRepository.save(newUser);

        return authenticate(new AuthRequest(request.email(), request.password()));
    }

    @Transactional
    public Role createRole(RoleDto request) {
        if (roleRepository.existsByName(request.name())) {
            throw new ApiRequestException(HttpStatus.CONFLICT, "Role com esse nome já existe");
        }

        Role role = new Role();
        BeanUtils.copyProperties(request, role); // Copia campos do DTO para o model
        return roleRepository.save(role);
    }
}
