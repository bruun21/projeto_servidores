package com.servidores.projeto.servidores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.model.CidadeModel;

@Repository
public interface CidadeRepository extends JpaRepository<CidadeModel, Long> {
}