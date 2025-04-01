package com.servidores.projeto.servidores.servidortemporario.dto;

import java.time.LocalDate;

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
public class ServidorTemporarioResponseDTO {
    private Long id;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private PessoaResponseDTO pessoa;
}
