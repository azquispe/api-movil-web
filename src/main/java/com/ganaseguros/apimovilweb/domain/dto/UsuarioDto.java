package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class UsuarioDto {

    private Long usuarioId;
    private Long personaId;
    private Long aplicacionId;
    private Long tipoUsuarioId;
    private String login;
    private String password;
    private Date fechaInicio;
    private Date fechaFin;

}
