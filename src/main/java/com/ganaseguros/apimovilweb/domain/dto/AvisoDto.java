package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class AvisoDto {
    private Long avisoId;
    private String titulo;
    private String contenido;
    private String enlace;
    private Long aplicacionId;
    private String fechaAviso;

}