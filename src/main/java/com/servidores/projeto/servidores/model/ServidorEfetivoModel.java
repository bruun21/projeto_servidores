package com.servidores.projeto.servidores.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "servidor_efetivo")
public class ServidorEfetivoModel {
    @Id
    @Column(name = "pes_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pes_id")
    private PessoaModel pessoa;

    @Column(name = "se_matricula", length = 20)
    private String matricula;

    // Getters e Setters
}