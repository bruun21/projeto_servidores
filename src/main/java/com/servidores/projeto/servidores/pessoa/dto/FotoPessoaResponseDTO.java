package com.servidores.projeto.servidores.pessoa.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FotoPessoaResponseDTO {
    
    private Long id;

    private LocalDate data;

    private String bucket;

    private String hash;

    private Long pessoaId;
}