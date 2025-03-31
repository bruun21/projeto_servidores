package com.servidores.projeto.servidores.unidade.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UnidadeRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200, message = "Nome deve ter até 200 caracteres")
    private String nome;

    @NotBlank(message = "Sigla é obrigatória")
    @Size(max = 20, message = "Sigla deve ter até 20 caracteres")
    private String sigla;

    private List<Long> idEnderecos;

}
