package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class AvisoDto {
    private Long avisoId;
    private String titulo;
    private String subtitulo;
    private String contenido;
    private Long documentoAdjuntoId;
    private Long aplicacionId;
    private String fechaAviso;
}
