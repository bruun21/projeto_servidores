package com.servidores.projeto.servidores.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.model.ServidorEfetivoModel;

@Repository
public interface ServidorEfetivoRepository extends CrudRepository<ServidorEfetivoModel, Long> {
    Page<ServidorEfetivoModel> findAll(Pageable pageable);
}
