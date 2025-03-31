package com.servidores.projeto.servidores.servidortemporario.model;


import java.time.LocalDate;

import com.servidores.projeto.servidores.pessoa.model.PessoaModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "servidor_temporario")
@Getter
@Setter
@Builder
public class ServidorTemporarioModel {

    @OneToOne
    @MapsId
    @Id
    @JoinColumn(name = "pes_id")
    private PessoaModel pessoa;

    @Column(name = "st_data_admissao")
    private LocalDate dataAdmissao;

    @Column(name = "st_data_demissao")
    private LocalDate dataDemissao;

}
