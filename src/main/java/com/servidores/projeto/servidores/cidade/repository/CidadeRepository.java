package com.servidores.projeto.servidores.cidade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.cidade.model.CidadeModel;

@Repository
public interface CidadeRepository extends JpaRepository<CidadeModel, Long> {
}