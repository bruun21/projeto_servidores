package com.servidores.projeto.servidores.endereco.model;

import java.util.ArrayList;
import java.util.List;

import com.servidores.projeto.servidores.cidade.model.CidadeModel;
import com.servidores.projeto.servidores.pessoa.model.PessoaModel;
import com.servidores.projeto.servidores.unidade.model.UnidadeModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "end_id")
    private Long id;

    @Column(name = "end_tipo_logradouro", length = 50)
    private String tipoLogradouro;

    @Column(name = "end_logradouro", length = 200)
    private String logradouro;

    @Column(name = "end_numero")
    private Integer numero;

    @Column(name = "end_bairro", length = 100)
    private String bairro;

    @ManyToOne
    @JoinColumn(name = "cid_id")
    private CidadeModel cidade;

    @ManyToMany(mappedBy = "enderecos")
    private List<PessoaModel> pessoas = new ArrayList<>();

    @ManyToMany(mappedBy = "enderecos")
    private List<UnidadeModel> unidades = new ArrayList<>();

}
