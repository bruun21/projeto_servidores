package com.servidores.projeto.servidores.servidortemporario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.servidortemporario.model.ServidorTemporarioModel;

@Repository
public interface ServidorTemporarioRepository extends JpaRepository<ServidorTemporarioModel, Long> {
}