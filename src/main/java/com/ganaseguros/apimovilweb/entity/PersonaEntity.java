package com.ganaseguros.apimovilweb.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "personas")
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long personaId;
    @Column(name = "genero_id")
    private Long generoId;
    @Column(name = "nombres")
    private String nombres;
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;
    @Column(name = "apellido_materno")
    private String apellidoMaterno;
    @Column(name = "apellido_esposo")
    private String apellidoEsposo;
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Column(name = "complemento")
    private String complemento;
    @Column(name = "ciudad_expedido_id")
    private Long ciudadExpedidoId;
    @Column(name = "numero_celular")
    private String numeroCelular;
    @Column(name = "correo_electronico")
    private String correoElectronico;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "estado_id")
    private Long estadoId;

}
