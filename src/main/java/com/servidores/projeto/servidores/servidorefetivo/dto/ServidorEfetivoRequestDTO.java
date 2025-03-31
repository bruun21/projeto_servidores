package com.servidores.projeto.servidores.servidorefetivo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServidorEfetivoRequestDTO {
    @NotNull(message = "Matrícula é obrigatória")
    private String matricula;
    
    @NotNull(message = "ID da Pessoa é obrigatório")
    private Long pessoaId;
}

