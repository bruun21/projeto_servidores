package com.servidores.projeto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Email é obrigatório") @Email(message = "Formato de email inválido") String email,

        @NotBlank(message = "Senha é obrigatória") @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres") String password,

        Integer roleId) {
}
