package com.ganaseguros.apimovilweb.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "documentos_adjuntos")
public class DocumentoAdjuntoEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documento_adjunto_id")
    private Long documentoAdjuntoId;
    @Column(name = "tipo_documento_id")
    private Long tipoDocumentoId;
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Column(name = "archivo")
    private String archivo;
    @Column(name = "extension_archivo")
    private String extensionArchivo;
    @Column(name = "estado_id")
    private Long estadoId;
}
