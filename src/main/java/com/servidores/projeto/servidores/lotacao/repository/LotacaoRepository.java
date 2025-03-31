package com.servidores.projeto.servidores.lotacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.lotacao.model.LotacaoModel;

@Repository
public interface LotacaoRepository extends JpaRepository<LotacaoModel, Long> {
}