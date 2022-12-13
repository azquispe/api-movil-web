package com.ganaseguros.apimovilweb.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "avisos")
public class AvisoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aviso_id")
    private Long avisoId;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "contenido")
    private String contenido;
    @Column(name = "enlace")
    private String enlace;
    @Column(name = "aplicacion_id")
    private Long aplicacionId;
    @Column(name = "fecha_aviso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAviso;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "estado_id")
    private Long estadoId;
}
