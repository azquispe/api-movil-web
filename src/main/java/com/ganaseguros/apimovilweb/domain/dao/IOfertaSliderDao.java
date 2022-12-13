package com.ganaseguros.apimovilweb.domain.dao;


import com.ganaseguros.apimovilweb.entity.OfertaSliderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOfertaSliderDao extends JpaRepository<OfertaSliderEntity,Long> {
    @Query("select a from OfertaSliderEntity a where a.estadoId = 1000 order by a.fechaRegistro desc")
    public List<OfertaSliderEntity> findAll();

    @Query("select a from OfertaSliderEntity a where a.estadoId = 1000 and a.aplicacionId = :pAplicacionID order by a.fechaRegistro desc")
    public List<OfertaSliderEntity> obtenerAvisosPorAplicacionID( Long pAplicacionID);

}
