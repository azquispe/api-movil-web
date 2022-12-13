package com.ganaseguros.apimovilweb.domain.dao;

import com.ganaseguros.apimovilweb.entity.DominioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface IDominioDao extends JpaRepository<DominioEntity, Long> {
    @Query("select d from DominioEntity d where d.dominio = :pDominio and d.estadoId = 1000 order by d.descripcion")
    public List<DominioEntity> findByDominio(@Param("pDominio") String pDominio);

    @Query("select d from DominioEntity d where d.dominioId = :pDominioId and d.estadoId = 1000 ")
    public Optional<DominioEntity> getDominioByDominioId(@Param("pDominioId") Long pDominioId);
}
