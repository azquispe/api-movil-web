package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.utils.FuncionesFechas;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class DocumentoStorageService {

    @Value("${url.file}")
    private String urlFile;

    @Value("${url.download_file}")
    private String urlDownloadfile;



    public ResponseDto saveFile(String pTipo, MultipartFile file) {
        ResponseDto  res  = new ResponseDto();
        try {
            String vNombreArchivo = FuncionesFechas.ObtenerFechaActualParaNombreArchivo() +"-"+ file.getOriginalFilename();
            String path = urlFile+"/"+pTipo;
            Files.copy(file.getInputStream(), Paths.get(path).resolve(vNombreArchivo));
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            String vPath = urlDownloadfile+"/"+pTipo+"/"+vNombreArchivo;
            res.setElementoGenerico(vPath);
            return res;
        } catch (Exception e) {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            return res;
        }
    }


    public Resource  downloadFile(String pFileName, String pTipo) {
        try {
            String vPath = urlFile+"/"+pTipo+"/"+pFileName;
            Resource resource = new UrlResource(Paths.get(vPath).toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            // .. almacenar log en BD
            return null;
        }
    }
}
