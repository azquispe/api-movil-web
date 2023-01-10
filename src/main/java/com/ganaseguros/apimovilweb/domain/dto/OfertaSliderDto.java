package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
public class OfertaSliderDto {
    private Long ofertaSliderId;
    private String titulo;
    private String subtitulo;
    private String contenido;
    private Long aplicacionId;
    private Long documentoAdjuntoId;
}
