package com.servidores.projeto.security.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleDto(
        @NotBlank(message = "Nome da role é obrigatório") String name,

        @NotBlank(message = "Descrição da role é obrigatória") String description) {
}
