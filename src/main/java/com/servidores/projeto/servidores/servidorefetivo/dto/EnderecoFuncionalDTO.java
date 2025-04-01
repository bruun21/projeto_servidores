package com.servidores.projeto.servidores.servidorefetivo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoFuncionalDTO {
    private String nomeServidor;
    private String matricula;
    private String unidadeNome;
    private String unidadeSigla;
    private List<EnderecoDTO> enderecosFuncionais;
}