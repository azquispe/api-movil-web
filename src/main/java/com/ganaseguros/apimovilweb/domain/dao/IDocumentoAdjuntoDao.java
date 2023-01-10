package com.ganaseguros.apimovilweb.domain.dao;

import com.ganaseguros.apimovilweb.entity.AvisoEntity;
import com.ganaseguros.apimovilweb.entity.DocumentoAdjuntoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDocumentoAdjuntoDao extends JpaRepository<DocumentoAdjuntoEntity,Long> {
}
