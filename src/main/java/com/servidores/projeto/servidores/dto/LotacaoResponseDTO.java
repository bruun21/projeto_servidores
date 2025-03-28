package com.servidores.projeto.servidores.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class LotacaoResponseDTO {
    private Long id;
    private Long pesId;
    private Long unidId;
    private LocalDate dataLotacao;
    private LocalDate dataRemocao;
    private String portaria;

}