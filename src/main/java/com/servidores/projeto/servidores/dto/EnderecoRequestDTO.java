package com.servidores.projeto.servidores.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnderecoRequestDTO {
    @NotBlank(message = "Tipo de logradouro é obrigatório")
    @Size(max = 50, message = "Tipo de logradouro deve ter até 50 caracteres")
    private String tipoLogradouro;

    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 200, message = "Logradouro deve ter até 200 caracteres")
    private String logradouro;

    @Min(value = 0, message = "Número deve ser um valor válido")
    private Integer numero;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "Bairro deve ter até 100 caracteres")
    private String bairro;

    @NotNull(message = "ID da cidade é obrigatório")
    private Long cidadeId;
}