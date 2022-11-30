package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.dto.ResponseDto;
import com.ganaseguros.apimovilweb.services.DominioService;
import com.ganaseguros.apimovilweb.services.EmailService;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/app-web")
public class DominioController {

    @Autowired
    private DominioService dominioService;

    @GetMapping(path = "/findByDominio/{pDominio}")
    public ResponseEntity<?> findByDominio(@PathVariable("pDominio") String pDominio) {

        Map<String, Object> response = new HashMap<>();
        ResponseDto res = dominioService.findByDominio(pDominio);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("dominios", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

    }
}
