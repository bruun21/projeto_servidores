package com.servidores.projeto.servidores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.model.ServidorEfetivoModel;

@Repository
public interface ServidorEfetivoRepository extends JpaRepository<ServidorEfetivoModel, Long> {
    
}
