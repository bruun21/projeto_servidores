package com.servidores.projeto.servidores.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UnidadeResponseDTO {
    private Long id;
    private String nome;
    private String sigla;
    List<EnderecoResponseDTO> enderecos;
}
