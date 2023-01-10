package com.ganaseguros.apimovilweb.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ofertas_slider")
public class OfertaSliderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oferta_slider_id")
    private Long ofertaSliderId;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "subtitulo")
    private String subtitulo;
    @Column(name = "contenido")
    private String contenido;

    @Column(name = "documento_adjunto_id")
    private Long documentoAdjuntoId;

    @Column(name = "aplicacion_id")
    private Long aplicacionId;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "estado_id")
    private Long estadoId;







}
