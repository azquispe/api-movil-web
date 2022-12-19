package com.ganaseguros.apimovilweb.domain.dao;

import com.ganaseguros.apimovilweb.entity.PersonaEntity;
import com.ganaseguros.apimovilweb.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IPersonaDao extends JpaRepository<PersonaEntity,Long> {
    @Query("select a from PersonaEntity a where a.estadoId = 1000 and a.personaId = :pPersonaId ")
    public Optional<PersonaEntity> obtenerPersonaPorID(Long pPersonaId);
}
