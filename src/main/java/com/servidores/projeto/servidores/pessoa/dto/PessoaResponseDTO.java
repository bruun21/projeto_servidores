package com.servidores.projeto.servidores.pessoa.dto;

import java.util.List;

import com.servidores.projeto.servidores.endereco.dto.EnderecoResponseDTO;
import com.servidores.projeto.servidores.lotacao.dto.LotacaoResponseDTO;

import lombok.Data;

@Data
public class PessoaResponseDTO {
    private Long id;

    private String nome;

    private String dataNascimento;

    private String sexo;

    private String mae;

    private String pai;

    private List<EnderecoResponseDTO> enderecos;
    private List<FotoPessoaResponseDTO> fotos;
    private List<LotacaoResponseDTO> lotacoes;
}