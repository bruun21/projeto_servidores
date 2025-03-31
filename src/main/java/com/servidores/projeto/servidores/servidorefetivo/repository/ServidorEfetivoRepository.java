package com.servidores.projeto.servidores.servidorefetivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.servidorefetivo.model.ServidorEfetivoModel;

@Repository
public interface ServidorEfetivoRepository extends JpaRepository<ServidorEfetivoModel, Long> {
    
}
