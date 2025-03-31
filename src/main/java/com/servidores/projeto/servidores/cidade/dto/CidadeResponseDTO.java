package com.servidores.projeto.servidores.cidade.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CidadeResponseDTO {
    private Long id;

    private String nome;

    private String uf;
}
