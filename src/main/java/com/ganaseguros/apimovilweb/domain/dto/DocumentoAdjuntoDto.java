package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class DocumentoAdjuntoDto {
    private Long documentoAdjuntoId;
    private Long tipoDocumentoId;
    private String nombreArchivo;
    private String archivo;
    private String extensionArchivo;
}
