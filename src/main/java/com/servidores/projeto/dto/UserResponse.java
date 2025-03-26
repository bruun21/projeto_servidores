package com.servidores.projeto.dto;

import java.time.LocalDateTime;

import com.servidores.projeto.model.User;

public record UserResponse(
    Long id,
    String email,
    User.Role role,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}