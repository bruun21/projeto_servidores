package com.servidores.projeto.servidores.servidortemporario.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServidorTemporarioRequestDTO {
    @NotNull(message = "Data de admissão é obrigatória")
    private LocalDate dataAdmissao;

    private LocalDate dataDemissao;

    @NotNull(message = "ID da Pessoa é obrigatório")
    private Long pessoaId;

}
