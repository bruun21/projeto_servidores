package com.servidores.projeto.servidores.servidorefetivo.model;

import com.servidores.projeto.servidores.pessoa.model.PessoaModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "servidor_efetivo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public ServidorEfetivoModel(PessoaModel pessoa, String matricula) {
        this.pessoa = pessoa;
        this.matricula = matricula;
    }

}
