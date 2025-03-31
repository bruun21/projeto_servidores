package com.servidores.projeto.servidores.cidade.model;

import java.util.ArrayList;
import java.util.List;

import com.servidores.projeto.servidores.endereco.model.EnderecoModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cidade")
@Getter
@Setter
@Builder
public class CidadeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid_id")
    private Long id;

    @Column(name = "cid_nome", length = 200)
    private String nome;

    @Column(name = "cid_uf", length = 2)
    private String uf;

    @OneToMany(mappedBy = "cidade")
    @Builder.Default
    private List<EnderecoModel> enderecos = new ArrayList<>();

}
