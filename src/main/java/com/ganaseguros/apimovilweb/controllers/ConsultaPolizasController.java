package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.dto.SolicitudSeguroDto;
import com.ganaseguros.apimovilweb.domain.services.ConsultaPolizasService;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/app-web")
public class ConsultaPolizasController {

    @Autowired
    private ConsultaPolizasService consultaPolizasService;

    @PostMapping("/v1/consulta-poliza")
    public ResponseEntity<?> consultaSegip(@RequestBody Map objRequest) {
        Map<String, Object> response = new HashMap<>();
        //List<Map<String, Object>> lstPolizas = appMovilService.consultaPoliza(vCi, vExtension,vFechaNac,vComplemento);
        ResponseDto res = consultaPolizasService.consultaPoliza( objRequest);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("datosPoliza", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }



    @GetMapping("/v1/descargar-poliza/{pPolicyId}")
    public ResponseEntity<?> descargarPoliza(@PathVariable String pPolicyId) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = consultaPolizasService.descargarPoliza(pPolicyId);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("documento", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }
}
