package com.ganaseguros.apimovilweb.dao;

import com.ganaseguros.apimovilweb.entity.AvisoMovilEntity;
import com.ganaseguros.apimovilweb.entity.DominioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAvisoMovilDao extends JpaRepository<AvisoMovilEntity,Long> {
    @Query("select a from AvisoMovilEntity a where a.estadoId = 1000 order by a.fechaAviso desc")
    public List<AvisoMovilEntity> findAll();
}
