package com.servidores.projeto.servidores.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EnderecoResponseDTO {
    private Long id;

    private String tipoLogradouro;

    private String logradouro;

    private Integer numero;

    private String bairro;

    private CidadeResponseDTO cidade;
}
