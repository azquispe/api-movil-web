package com.ganaseguros.apimovilweb.domain.dao;

import com.ganaseguros.apimovilweb.entity.AvisoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAvisoDao extends JpaRepository<AvisoEntity,Long> {
    @Query("select a from AvisoEntity a where a.estadoId = 1000 order by a.fechaAviso desc")
    public List<AvisoEntity> findAll();

    @Query("select a from AvisoEntity a where a.estadoId = 1000 and a.aplicacionId = :pAplicacionId order by a.fechaAviso desc")
    public List<AvisoEntity> obtenerAvisosPorAplicacionID(Long pAplicacionId);
}
