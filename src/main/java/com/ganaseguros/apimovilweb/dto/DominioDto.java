package com.ganaseguros.apimovilweb.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
@Data
public class DominioDto {
    private Integer dominioId;
    private String dominio;
    private String descripcion;
    private String abreviatura;

}
