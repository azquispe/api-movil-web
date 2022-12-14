package com.ganaseguros.apimovilweb.domain.dao;


import com.ganaseguros.apimovilweb.entity.OfertaSliderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IOfertaSliderDao extends JpaRepository<OfertaSliderEntity,Long> {
    @Query("select a from OfertaSliderEntity a where a.estadoId = 1000 order by a.fechaRegistro desc")
    public List<OfertaSliderEntity> obtenerOfertasTodos();

    @Query("select a from OfertaSliderEntity a where a.estadoId = 1000 and a.aplicacionId = :pAplicacionID order by a.fechaRegistro desc")
    public List<OfertaSliderEntity> obtenerOfertasPorAplicacionID( Long pAplicacionID);

    @Query("select a from OfertaSliderEntity a where a.estadoId = 1000 and a.ofertaSliderId = :pOfertaSliderId order by a.fechaRegistro desc")
    public Optional<OfertaSliderEntity> obtenerOfertasPorID(Long pOfertaSliderId);

}
