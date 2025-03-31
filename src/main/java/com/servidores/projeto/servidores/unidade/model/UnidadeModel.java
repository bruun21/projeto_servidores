package com.servidores.projeto.servidores.unidade.model;

import java.util.ArrayList;
import java.util.List;

import com.servidores.projeto.servidores.endereco.model.EnderecoModel;
import com.servidores.projeto.servidores.lotacao.model.LotacaoModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "unidade")
@Getter
@Setter
@Builder
public class UnidadeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unid_id")
    private Long id;

    @Column(name = "unid_nome", length = 200)
    private String nome;

    @Column(name = "unid_sigla", length = 20)
    private String sigla;

    @ManyToMany
    @JoinTable(name = "unidade_endereco", joinColumns = @JoinColumn(name = "unid_id"), inverseJoinColumns = @JoinColumn(name = "end_id"))
    @Builder.Default
    private List<EnderecoModel> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "unidade")
    @Builder.Default
    private List<LotacaoModel> lotacoes = new ArrayList<>();

}
