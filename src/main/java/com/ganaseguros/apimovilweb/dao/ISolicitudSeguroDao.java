package com.ganaseguros.apimovilweb.dao;

import com.ganaseguros.apimovilweb.entity.DominioEntity;
import com.ganaseguros.apimovilweb.entity.SolicitudSeguroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudSeguroDao extends JpaRepository<SolicitudSeguroEntity, Long> {

}
