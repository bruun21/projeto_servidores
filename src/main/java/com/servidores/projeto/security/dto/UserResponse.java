package com.servidores.projeto.security.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
                Long id,
                String email,
                Set<RoleDto> role,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
