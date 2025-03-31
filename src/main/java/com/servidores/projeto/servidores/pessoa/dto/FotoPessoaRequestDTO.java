package com.servidores.projeto.servidores.pessoa.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FotoPessoaRequestDTO {
    
    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @NotBlank(message = "Bucket é obrigatório")
    @Size(max = 50, message = "Bucket deve ter até 50 caracteres")
    private String bucket;

    @NotBlank(message = "Hash é obrigatório")
    @Size(max = 50, message = "Hash deve ter até 50 caracteres")
    private String hash;

    @NotNull(message = "ID da pessoa é obrigatório")
    private Long pessoaId;
}