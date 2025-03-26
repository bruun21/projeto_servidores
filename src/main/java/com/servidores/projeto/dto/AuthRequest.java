package com.servidores.projeto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    String email,
    
    @NotBlank(message = "Senha é obrigatória")
    String password
) {}