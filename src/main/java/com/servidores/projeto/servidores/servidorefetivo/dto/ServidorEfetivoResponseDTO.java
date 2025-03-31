package com.servidores.projeto.servidores.servidorefetivo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServidorEfetivoResponseDTO {
    private Long id;
    private String matricula;
    private Long pesId;
}