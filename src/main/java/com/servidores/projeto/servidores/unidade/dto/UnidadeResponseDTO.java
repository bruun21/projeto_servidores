package com.servidores.projeto.servidores.unidade.dto;

import java.util.List;

import com.servidores.projeto.servidores.endereco.dto.EnderecoResponseDTO;

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
