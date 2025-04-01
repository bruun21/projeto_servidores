package com.servidores.projeto.servidores.servidorefetivo.dto;

import com.servidores.projeto.servidores.pessoa.dto.PessoaResponseDTO;

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
public class ServidorEfetivoResponseDTO {
    private Long id;
    private String matricula;
    private PessoaResponseDTO pessoa;
}