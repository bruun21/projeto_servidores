package com.servidores.projeto.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        RoleDto role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
