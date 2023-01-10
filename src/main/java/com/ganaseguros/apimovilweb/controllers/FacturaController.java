package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.services.FacturaService;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/app-web")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    @PostMapping("/v1/consulta-facturas")
    public ResponseEntity<?> consultaFacturas(@RequestBody Map objRequest) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = facturaService.consultaFacturas( objRequest);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("datosFactura", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    @GetMapping("/v1/descargar-factura")
    public ResponseEntity<?> descargarPoliza() {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = facturaService.descargarFactura();
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("documento", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }
}
