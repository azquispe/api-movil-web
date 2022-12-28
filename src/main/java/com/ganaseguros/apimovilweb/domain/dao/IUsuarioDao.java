package com.ganaseguros.apimovilweb.domain.dao;

import com.ganaseguros.apimovilweb.entity.AvisoEntity;
import com.ganaseguros.apimovilweb.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUsuarioDao  extends JpaRepository<UsuarioEntity,Long> {
    @Query("select a from UsuarioEntity a where a.estadoId = 1000 and a.usuarioId = :pUsuarioId ")
    public Optional<UsuarioEntity> obtenerUsuarioPorID(Long pUsuarioId);

    @Query("select a from UsuarioEntity a where a.estadoId = 1000 and a.personaId = :pPersonaId ")
    public Optional<UsuarioEntity> obtenerUsuarioPorPersonaId(Long pPersonaId);

    @Query("select a from UsuarioEntity a where a.estadoId = 1000 and a.login = :pLogin ")
    public Optional<UsuarioEntity> obtenerUsuarioPorLogin(String pLogin);

}
