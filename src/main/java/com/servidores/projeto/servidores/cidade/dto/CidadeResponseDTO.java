package com.servidores.projeto.servidores.cidade.dto;

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
public class CidadeResponseDTO {
    private Long id;

    private String nome;

    private String uf;
}
