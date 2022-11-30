package com.ganaseguros.apimovilweb.dto;

import lombok.Data;

import java.util.List;
@Data
public class PolizaDto {
       private String polizaId;
       private String nombreProducto;
       private String numeroProducto;
       private String nombreAsegurado;
       private String nombreTomador;
       private List<String> lstBeneficiarios;
       private String numeroOperacion;
       private String nombrePoliza;
       private String fechaInicio;
       private String fechaFin;
       private String estado;
       private String tipoProducto;
       private String frecuencia;
       private String montoPrima;
       private String precio;
       private Boolean tieneDocumentoPoliza;

}
