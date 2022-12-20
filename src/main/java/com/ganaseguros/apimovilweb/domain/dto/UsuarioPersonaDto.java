package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class UsuarioPersonaDto {
    private UsuarioDto usuarioDto;
    private PersonaDto personaDto;
}
