package com.servidores.projeto.servidores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.model.UnidadeModel;

@Repository
public interface UnidadeRepository extends JpaRepository<UnidadeModel, Long> {
}