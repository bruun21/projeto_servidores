package com.servidores.projeto.servidores.lotacao.dto;

import java.time.LocalDate;

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
    private Long pesId;
    private Long unidId;
    private LocalDate dataLotacao;
    private LocalDate dataRemocao;
    private String portaria;

}