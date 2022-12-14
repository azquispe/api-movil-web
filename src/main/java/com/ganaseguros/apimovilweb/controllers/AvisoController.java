package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.domain.dto.AvisoDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.services.AvisoService;
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
public class AvisoController {

    @Autowired
    private AvisoService avisoService;

    @GetMapping("/v1/obtener-avisos/{pAplicacionID}")
    public ResponseEntity<?> obtenerAvisosPorAplicacionID(@PathVariable Long pAplicacionID) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = avisoService.obtenerAvisosPorAplicacionID(pAplicacionID);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("avisos", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    @GetMapping("/v1/obtener-avisos-todos")
    public ResponseEntity<?> obtenerAvisosTodos() {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = avisoService.obtenerAvisosTodos();
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("avisos", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PostMapping("/v1/registrar-aviso")
    public ResponseEntity<?> registrarAvisos(@RequestBody AvisoDto pAvisoDto) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = avisoService.insertAviso(pAvisoDto);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("avisos", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @DeleteMapping("/v1/eliminar-aviso/{pAvisoId}")
    public ResponseEntity<?> eliminarAviso(@PathVariable Long pAvisoId) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = avisoService.eliminarAvisoPorID (pAvisoId);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
