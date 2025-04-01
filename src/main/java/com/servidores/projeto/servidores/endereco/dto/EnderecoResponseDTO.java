package com.servidores.projeto.servidores.endereco.dto;

import com.servidores.projeto.servidores.cidade.dto.CidadeResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoResponseDTO {
    private Long id;

    private String tipoLogradouro;

    private String logradouro;

    private Integer numero;

    private String bairro;

    private CidadeResponseDTO cidade;
}
