package com.servidores.projeto.servidores.cidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CidadeRequestDTO {
    @NotBlank(message = "Nome da cidade é obrigatório")
    @Size(max = 100, message = "Nome da cidade deve ter até 100 caracteres")
    private String nome;

    @NotBlank(message = "UF é obrigatório")
    @Size(min = 2, max = 2, message = "UF deve ser uma sigla de 2 caracteres (ex: SP, RJ)")
    private String uf;
}