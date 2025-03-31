package com.servidores.projeto.servidores.pessoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.pessoa.model.FotoPessoaModel;

@Repository
public interface FotoPessoaRepository extends JpaRepository<FotoPessoaModel, Long> {
}