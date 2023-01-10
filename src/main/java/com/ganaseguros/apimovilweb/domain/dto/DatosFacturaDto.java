package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

@Data
public class DatosFacturaDto {
    private String fechaFactura;
    private String nitEmisor;
    private String nombreComercial;
    private String numeroFactura;
    private String monto;
}
