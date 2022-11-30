package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.dto.AvisoMovilDto;
import com.ganaseguros.apimovilweb.dto.ResponseDto;
import com.ganaseguros.apimovilweb.services.AvisoMovilService;
import com.ganaseguros.apimovilweb.services.DominioService;
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
public class AvisoMovilController {

    @Autowired
    private AvisoMovilService avisoMovilService;

    @GetMapping("/v1/obtener-avisos")
    public ResponseEntity<?> obtenerAvisos() {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = avisoMovilService.findAll();
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("avisos", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PostMapping("/v1/registrar-aviso")
    public ResponseEntity<?> registrarAvisos(@RequestBody AvisoMovilDto avisoMovilDto) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = avisoMovilService.insertAvisoMovil(avisoMovilDto);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("avisos", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
