package com.ganaseguros.apimovilweb.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "dominios")
public class DominioEntity {
    @Id
    @Column(name = "dominio_id")
    private Integer dominioId;
    @Column(name = "dominio")
    private String dominio;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "abreviatura")
    private String abreviatura;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "estado_id")
    private Integer estadoId;
}
