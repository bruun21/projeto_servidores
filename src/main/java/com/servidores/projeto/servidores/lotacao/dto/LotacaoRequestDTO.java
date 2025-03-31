package com.servidores.projeto.servidores.lotacao.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class LotacaoRequestDTO {
    @NotNull(message = "ID da Pessoa é obrigatório")
    private Long pessoaId;
    
    @NotNull(message = "ID da Unidade é obrigatório")
    private Long unidadeId;
    
    @NotNull(message = "Data de lotação é obrigatória")
    private LocalDate dataLotacao;
    
    private LocalDate dataRemocao;
    
    @Size(max = 100, message = "Portaria deve ter até 100 caracteres")
    private String portaria;
}
