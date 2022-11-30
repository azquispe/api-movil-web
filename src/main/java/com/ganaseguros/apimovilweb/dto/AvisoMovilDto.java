package com.ganaseguros.apimovilweb.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class AvisoMovilDto {
    private Integer avisosMovilId;
    private String titulo;
    private String contenido;
    private String enlace;
    private String ruta;
    private Date fechaAviso;
    private Date fechaRegistro;
    private Date fechaModificacion;

}
