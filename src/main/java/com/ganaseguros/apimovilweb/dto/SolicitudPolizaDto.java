package com.ganaseguros.apimovilweb.dto;


import lombok.Data;

@Data
public class SolicitudPolizaDto {
    private String nombres;
    private String apellidos;
    private String telefonoCelular;
    private String correo;
    private String ciudad;
    private Integer tipoProductoId;
    private Boolean tieneSeguroConNosotros;
    private Boolean tieneSeguroConOtros;
    private String descripcion;
    private Integer tipoMedioSolicitudSeguroId;
}
