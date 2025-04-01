package com.servidores.projeto.servidores.servidorefetivo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO {
    
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
}