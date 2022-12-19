package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class UsuarioPersonaDto {
    private Long usuarioId;
    private Long generoId;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String apellidoEsposo;
    private String numeroDocumento;
    private String complemento;
    private Long ciudadExpedidoId;
    private String numeroCelular;
    private String correoElectronico;
    private String fechaNacimiento;
}
