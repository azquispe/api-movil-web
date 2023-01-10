package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.domain.dto.AvisoDto;
import com.ganaseguros.apimovilweb.domain.dto.PersonaDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.dto.UsuarioPersonaDto;
import com.ganaseguros.apimovilweb.domain.services.UsuarioService;
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
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/v1/registrar-usuario")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioPersonaDto pUsuarioPersonaDto) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = usuarioService.insertarUsuarioPersona(pUsuarioPersonaDto);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    @PutMapping("/v1/actualizar-usuario")
    public ResponseEntity<?> actualizarUsuario(@RequestBody PersonaDto pPersonaDto) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = usuarioService.actualizarUsuarioPersona(pPersonaDto);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    @PutMapping("/v1/actualizar-clave")
    public ResponseEntity<?> actualizarClave(@RequestBody Map objRequest) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = usuarioService.actualizarClave(objRequest);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    @PostMapping("/v1/autentication")
    public ResponseEntity<?> autentication(@RequestBody Map objRequest) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = usuarioService.autentication( objRequest);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("datosPersona", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    @GetMapping("/v1/obtener-persona/{pPersonaId}")
    public ResponseEntity<?> obtenerPersona(@PathVariable Long pPersonaId) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = usuarioService.obtenerPersonaPorId( pPersonaId);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());
        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("datosPersona", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}