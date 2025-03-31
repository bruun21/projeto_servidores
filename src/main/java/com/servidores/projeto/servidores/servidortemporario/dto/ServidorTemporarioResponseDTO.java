package com.servidores.projeto.servidores.servidortemporario.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServidorTemporarioResponseDTO {
    private Long id;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private Long pesId;

    // Getters e Setters
}
