package com.ganaseguros.apimovilweb.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "solicitud_seguro")
public class SolicitudSeguroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitud_seguro_id")
    private Integer solicitudSeguroId;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "telefono_celular")
    private String telefono_celular;
    @Column(name = "correo")
    private String correo;
    @Column(name = "ciudad")
    private String ciudad;
    @Column(name = "tiene_seguro_nosotros")
    private boolean tieneSeguroNosotros;
    @Column(name = "tiene_seguro_otros")
    private boolean tieneSeguroOtros;
    @Column(name = "creado_crm")
    private boolean creadoCrm;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "tipo_medio_solicitud_seguro_id")
    private Integer tipoMedioSolicitudSeguroId;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "estado_id")
    private Integer estadoId;

}
