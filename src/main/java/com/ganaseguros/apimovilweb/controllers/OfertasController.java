package com.ganaseguros.apimovilweb.controllers;



import com.ganaseguros.apimovilweb.domain.dto.OfertaSliderDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.services.OfertaSliderService;
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
public class OfertasController {


    @Autowired
    private OfertaSliderService ofertaService;

    @GetMapping("/v1/obtener-ofertas/{pAplicacionID}")
    public ResponseEntity<?> obtenerOfertas(@PathVariable Long pAplicacionID) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = ofertaService.obtenerAvisosPorAplicacionID(pAplicacionID);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("ofertas", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @PostMapping("/v1/registrar-ofertas")
    public ResponseEntity<?> registrarAvisos(@RequestBody OfertaSliderDto pOfertaSliderDto) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = ofertaService.insertOfertaSlider(pOfertaSliderDto);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("avisos", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
