package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.services.DocumentoAdjuntoService;
import com.ganaseguros.apimovilweb.domain.services.DocumentoStorageService;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/app-web")
public class DocumentoAdjuntoController {

    @Autowired
    private DocumentoAdjuntoService documentoAdjuntoService;

    @PostMapping("/v1/upload-file/{pTipo}")
    public ResponseEntity<?> uploadFile(@RequestParam("archivo") MultipartFile pArchivo, @PathVariable Long pTipo) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = documentoAdjuntoService.insertarDocumentoAdjunto(pArchivo,pTipo);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());

        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("documentoAdjuntoId", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    @GetMapping("/v1/download-file/{pDocumentoAdjuntoId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long pDocumentoAdjuntoId){
        ResponseDto resDto =  documentoAdjuntoService.descargarDocumentoAdjunto(pDocumentoAdjuntoId);
        return  (ResponseEntity<byte[]>)resDto.getElementoGenerico();
    }
}
