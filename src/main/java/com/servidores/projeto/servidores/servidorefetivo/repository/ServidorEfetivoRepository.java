package com.servidores.projeto.servidores.servidorefetivo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servidores.projeto.servidores.servidorefetivo.model.ServidorEfetivoModel;

@Repository
public interface ServidorEfetivoRepository extends JpaRepository<ServidorEfetivoModel, Long> {
    @Query("SELECT s FROM ServidorEfetivoModel s " +
            "JOIN s.pessoa p " +
            "JOIN p.lotacoes l " +
            "WHERE l.unidade.id = :unidId " +
            "AND l.dataRemocao IS NULL")
    List<ServidorEfetivoModel> findByUnidadeLotacao(@Param("unidId") Long unidId);
}
