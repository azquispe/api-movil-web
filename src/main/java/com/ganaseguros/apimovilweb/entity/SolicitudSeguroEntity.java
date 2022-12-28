package com.ganaseguros.apimovilweb.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "solicitud_seguros")
public class SolicitudSeguroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitud_seguro_id")
    private Long solicitudSeguroId;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "telefono_celular")
    private String telefonoCelular;
    @Column(name = "correo")
    private String correo;
    @Column(name = "ciudad_id")
    private Long ciudadId;
    @Column(name = "tipo_producto_id")
    private Long tipoProductoId;
    @Column(name = "tiene_seguro_nosotros")
    private boolean tieneSeguroNosotros;
    @Column(name = "tiene_seguro_otros")
    private boolean tieneSeguroOtros;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "creado_crm")
    private boolean creadoCrm;
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
