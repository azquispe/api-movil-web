package com.ganaseguros.apimovilweb.domain.dao;

import com.ganaseguros.apimovilweb.entity.AvisoEntity;
import com.ganaseguros.apimovilweb.entity.OfertaSliderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAvisoDao extends JpaRepository<AvisoEntity,Long> {
    @Query("select a from AvisoEntity a where a.estadoId = 1000 order by a.fechaAviso desc")
    public List<AvisoEntity> obtenerAvisosTodos();

    @Query("select a from AvisoEntity a where a.estadoId = 1000 and a.aplicacionId = :pAplicacionId order by a.fechaAviso desc")
    public List<AvisoEntity> obtenerAvisosPorAplicacionID(Long pAplicacionId);

    @Query("select a from AvisoEntity a where a.estadoId = 1000 and a.avisoId = :pAvisoId order by a.fechaRegistro desc")
    public Optional<AvisoEntity> obtenerAvisoPorID(Long pAvisoId);


}
