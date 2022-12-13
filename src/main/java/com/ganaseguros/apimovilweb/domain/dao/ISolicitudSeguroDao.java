package com.ganaseguros.apimovilweb.domain.dao;

import com.ganaseguros.apimovilweb.entity.SolicitudSeguroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudSeguroDao extends JpaRepository<SolicitudSeguroEntity, Long> {

}
