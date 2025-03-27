package com.servidores.projeto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.servidores.projeto.dto.AuthRequest;
import com.servidores.projeto.dto.AuthResponse;
import com.servidores.projeto.dto.RefreshTokenRequest;
import com.servidores.projeto.dto.RoleDto;
import com.servidores.projeto.dto.UserRequest;
import com.servidores.projeto.dto.UserResponse;
import com.servidores.projeto.exception.ApiRequestException;
import com.servidores.projeto.exception.AuthException;
import com.servidores.projeto.exception.NotFoundException;
import com.servidores.projeto.exception.TokenRefreshException;
import com.servidores.projeto.mapper.UserMapper;
import com.servidores.projeto.model.Role;
import com.servidores.projeto.model.User;
import com.servidores.projeto.repository.RoleRepository;
import com.servidores.projeto.repository.UserRepository;

public class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @Spy
    @InjectMocks
    private AuthService authService;

    @Mock
    private Logger log;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate_Success() {

        AuthRequest request = new AuthRequest("test@example.com", "password123");
        User user = new User();
        user.setEmail("test@example.com");
        Authentication auth = mock(Authentication.class);
        UserResponse userResponse = new UserResponse(
                1L,
                "test@example.com",
                new RoleDto("ROLE_USER", "User role"),
                LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.of(2023, 1, 2, 15, 0));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("accessToken123");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken123");
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        AuthResponse result = authService.authenticate(request);

        assertNotNull(result);
        assertEquals("accessToken123", result.accessToken());
        assertEquals("refreshToken123", result.refreshToken());
        assertEquals("Bearer", result.tokenType());
        assertEquals(userResponse, result.user());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateAccessToken(user);
        verify(jwtService, times(1)).generateRefreshToken(user);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void testAuthenticate_BadCredentials() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.authenticate(request);
        });

        assertEquals("Combinação email/senha incorreta", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateAccessToken(any(User.class));
        verify(jwtService, never()).generateRefreshToken(any(User.class));
    }

    @Test
    void testAuthenticate_DisabledAccount() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("Conta desativada"));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.authenticate(request);
        });

        assertEquals("Conta desativada", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateAccessToken(any(User.class));
    }

    @Test
    void testAuthenticate_UnexpectedError() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Erro inesperado"));

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.authenticate(request);
        });

        assertEquals("Erro interno durante autenticação", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateAccessToken(any(User.class));
    }

    @Test
    void testRefreshToken_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("validRefreshToken");
        User user = new User();
        user.setEmail("test@example.com");
        UserResponse userResponse = new UserResponse(
                1L,
                "test@example.com",
                new RoleDto("ROLE_USER", "User role"),
                LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.of(2023, 1, 2, 15, 0));

        when(jwtService.isValidRefreshToken("validRefreshToken")).thenReturn(true);
        when(jwtService.extractUsername("validRefreshToken")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        AuthResponse result = authService.refreshToken(request);

        assertNotNull(result);
        assertEquals("newAccessToken", result.accessToken());
        assertEquals("validRefreshToken", result.refreshToken());
        assertEquals("Bearer", result.tokenType());
        assertEquals(userResponse, result.user());
        verify(jwtService, times(1)).isValidRefreshToken("validRefreshToken");
        verify(jwtService, times(1)).extractUsername("validRefreshToken");
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtService, times(1)).generateToken(user);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void testRefreshToken_InvalidToken() {
        RefreshTokenRequest request = new RefreshTokenRequest("invalidRefreshToken");

        when(jwtService.isValidRefreshToken("invalidRefreshToken")).thenReturn(false);

        TokenRefreshException exception = assertThrows(TokenRefreshException.class, () -> {
            authService.refreshToken(request);
        });

        assertEquals("Refresh token inválido ou expirado", exception.getMessage());
        verify(jwtService, times(1)).isValidRefreshToken("invalidRefreshToken");
        verify(jwtService, never()).extractUsername(anyString());
        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    void testRegister_Success() {
        // Arrange
        UserRequest request = new UserRequest("test@example.com", "password123", 1L);
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        User newUser = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(role)
                .enabled(true)
                .build();
        AuthResponse authResponse = new AuthResponse("accessToken", "refreshToken", "Bearer", null);

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Mockando o método authenticate usando doReturn para o spy
        doReturn(authResponse).when(authService).authenticate(any(AuthRequest.class));

        // Act
        AuthResponse result = authService.register(request);

        // Assert
        assertNotNull(result);
        assertEquals("accessToken", result.accessToken());
        assertEquals("refreshToken", result.refreshToken());
        assertEquals("Bearer", result.tokenType());
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(roleRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verify(authService, times(1)).authenticate(any(AuthRequest.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // Arrange
        UserRequest request = new UserRequest("test@example.com", "password123", 1L);

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.register(request);
        });

        assertEquals("Email já registrado", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(roleRepository, never()).findById(anyLong());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(authService, never()).authenticate(any(AuthRequest.class));
    }

    @Test
    void testRegister_RoleNotFound() {
        // Arrange
        UserRequest request = new UserRequest("test@example.com", "password123", 1L);

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            authService.register(request);
        });

        assertEquals("Role não encontrada", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(roleRepository, times(1)).findById(1L);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(authService, never()).authenticate(any(AuthRequest.class));
    }

    @Test
    void testCreateRole_Success() {
        RoleDto roleDto = new RoleDto("ROLE_USER", "User role");

        when(roleRepository.existsByName("ROLE_USER")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
            Role role = invocation.getArgument(0);
            role.setId(1L);
            return role;
        });

        Role result = authService.createRole(roleDto);

        assertNotNull(result);
        assertEquals("ROLE_USER", result.getName());
        assertEquals("User role", result.getDescription());
        assertEquals(1L, result.getId());
        verify(roleRepository, times(1)).existsByName("ROLE_USER");
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testCreateRole_Conflict() {
        RoleDto roleDto = new RoleDto("ROLE_USER", null);

        when(roleRepository.existsByName("ROLE_USER")).thenReturn(true);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            authService.createRole(roleDto);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Role com esse nome já existe", exception.getMessage());
        verify(roleRepository, times(1)).existsByName("ROLE_USER");
        verify(roleRepository, never()).save(any(Role.class));
    }
}
