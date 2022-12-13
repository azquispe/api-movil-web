package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
@Data
public class DominioDto {
    private Long dominioId;
    private String dominio;
    private String descripcion;
    private String abreviatura;

}
