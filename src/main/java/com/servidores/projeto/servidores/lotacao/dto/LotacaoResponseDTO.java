package com.servidores.projeto.servidores.lotacao.dto;

import java.time.LocalDate;

import com.servidores.projeto.servidores.pessoa.dto.PessoaGetDTO;
import com.servidores.projeto.servidores.unidade.dto.UnidadeResponseDTO;

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
public class LotacaoResponseDTO {
    private Long id;
    private PessoaGetDTO pessoa;
    private UnidadeResponseDTO unidade;
    private LocalDate dataLotacao;
    private LocalDate dataRemocao;
    private String portaria;
}
