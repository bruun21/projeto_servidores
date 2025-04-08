package com.servidores.projeto.servidores.pessoa.dto;

import java.util.List;

import com.servidores.projeto.servidores.endereco.dto.EnderecoResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponseDTO {
    private Long id;

    private String nome;

    private String dataNascimento;

    private String sexo;

    private String mae;

    private String pai;

    private List<EnderecoResponseDTO> enderecos;
    private List<FotoPessoaResponseDTO> fotos;
}
