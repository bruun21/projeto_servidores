package com.servidores.projeto.servidores.unidade.dto;

import java.util.List;

import com.servidores.projeto.servidores.endereco.dto.EnderecoResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnidadeResponseDTO {
    private Long id;
    private String nome;
    private String sigla;
    List<EnderecoResponseDTO> enderecos;
}
