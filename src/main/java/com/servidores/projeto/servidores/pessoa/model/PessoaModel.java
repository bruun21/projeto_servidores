package com.servidores.projeto.servidores.pessoa.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.servidores.projeto.servidores.endereco.model.EnderecoModel;
import com.servidores.projeto.servidores.lotacao.model.LotacaoModel;
import com.servidores.projeto.servidores.servidorefetivo.model.ServidorEfetivoModel;
import com.servidores.projeto.servidores.servidortemporario.model.ServidorTemporarioModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@Builder
public class PessoaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pes_id")
    private Long id;

    @Column(name = "pes_nome", length = 200)
    private String nome;

    @Column(name = "pes_data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "pes_sexo", length = 9)
    private String sexo;

    @Column(name = "pes_mae", length = 200)
    private String mae;

    @Column(name = "pes_pai", length = 200)
    private String pai;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FotoPessoaModel> fotos = new ArrayList<>();

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private ServidorTemporarioModel servidorTemporario;

    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private ServidorEfetivoModel servidorEfetivo;

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "pessoa_endereco", joinColumns = @JoinColumn(name = "pes_id"), inverseJoinColumns = @JoinColumn(name = "end_id"))
    private List<EnderecoModel> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "pessoa")
    @Builder.Default
    private List<LotacaoModel> lotacoes = new ArrayList<>();
}
