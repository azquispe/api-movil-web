package com.ganaseguros.apimovilweb.domain.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private String codigo;
    private String mensaje;
    private T elementoGenerico;
}
