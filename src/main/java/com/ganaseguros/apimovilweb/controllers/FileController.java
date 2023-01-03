package com.ganaseguros.apimovilweb.controllers;

import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.services.FilesStorageService;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/app-web")
public class FileController {

    @Autowired
    private FilesStorageService filesStorageService;

    @PostMapping("/v1/upload-file/{pTipo}")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile pArchivo, @PathVariable String pTipo) {
        Map<String, Object> response = new HashMap<>();
        ResponseDto res = filesStorageService.saveFile(pTipo, pArchivo);
        response.put("codigoMensaje", res.getCodigo());
        response.put("mensaje", res.getMensaje());

        if(res.getCodigo().equals(ConstDiccionarioMensaje.CODMW1000))
            response.put("path", res.getElementoGenerico());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    @GetMapping("/v1/download-file/{pTipo}/{pNameFile}")
    public ResponseEntity<Resource> descargarAdjuntoRecepcion(@PathVariable String pTipo,@PathVariable String pNameFile){

        Map<String, Object> response = new HashMap<>();
        Resource resource = filesStorageService.downloadFile(pNameFile,pTipo);

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }else{
            return null;
        }

    }
}
