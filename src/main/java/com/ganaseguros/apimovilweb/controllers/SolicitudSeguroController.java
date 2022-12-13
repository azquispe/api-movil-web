package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.dto.SolicitudSeguroDto;
import com.ganaseguros.apimovilweb.domain.services.ConsultaPolizasService;
import com.ganaseguros.apimovilweb.domain.services.SolicitudSeguroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/app-web")
public class SolicitudSeguroController {
    @Autowired
    private SolicitudSeguroService solicitudSeguroService;

    @PostMapping("/v1/solicitud-seguro")
    public ResponseEntity<?> solicitudSeguro(@RequestBody SolicitudSeguroDto pSolicitudPolizaDto) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto result = solicitudSeguroService.enviarSolicitudSeguro(pSolicitudPolizaDto);
        response.put("codigoMensaje", result.getCodigo());
        response.put("mensaje",result.getMensaje());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
