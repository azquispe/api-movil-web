package com.ganaseguros.apimovilweb.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private String codigo;
    private String mensaje;
    private T elementoGenerico;
}
