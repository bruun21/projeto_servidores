package com.servidores.projeto.servidores.pessoa.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaGetDTO {
    private Long id;

    private String nome;

    private String dataNascimento;

    private String sexo;

    private String mae;

    private String pai;

    private List<FotoPessoaResponseDTO> fotos;
}