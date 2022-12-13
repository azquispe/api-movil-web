package com.ganaseguros.apimovilweb.domain.dto;


import lombok.Data;

import javax.persistence.Column;

@Data
public class SolicitudSeguroDto {
    private Long solicitudSeguroId;
    private String nombres;
    private String apellidos;
    private String telefonoCelular;
    private String correo;
    private String ciudad;
    private Long tipoProductoId;
    private boolean tieneSeguroNosotros;
    private boolean tieneSeguroOtros;
    private String descripcion;
    private boolean creadoCrm;
    private Long aplicacionId;

}
