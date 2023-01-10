package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dao.IAvisoDao;
import com.ganaseguros.apimovilweb.domain.dao.IDocumentoAdjuntoDao;
import com.ganaseguros.apimovilweb.domain.dto.AvisoDto;
import com.ganaseguros.apimovilweb.domain.dto.DocumentoAdjuntoDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.entity.AvisoEntity;
import com.ganaseguros.apimovilweb.entity.DocumentoAdjuntoEntity;
import com.ganaseguros.apimovilweb.utils.FuncionesGenericos;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class DocumentoAdjuntoService {
    @Autowired
    private IDocumentoAdjuntoDao iDocumentoAdjuntoDao;

    public ResponseDto insertarDocumentoAdjunto(MultipartFile archivo,Long  pTipoDocumentoId){
        ResponseDto res = new ResponseDto();
        try{

            String vNombreArchivo =  archivo.getName();
            String vExtension = archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf(".") + 1);
            String vArchivoBase64 = FuncionesGenericos.convertToBase64FromMultipartFile(archivo.getInputStream());
            DocumentoAdjuntoEntity insert = new DocumentoAdjuntoEntity();
            insert.setTipoDocumentoId(pTipoDocumentoId);
            insert.setNombreArchivo(vNombreArchivo);
            insert.setArchivo(vArchivoBase64);
            insert.setExtensionArchivo(vExtension);
            insert.setEstadoId(ConstEstado.ACTIVO);
            iDocumentoAdjuntoDao.save(insert);

            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(insert.getDocumentoAdjuntoId());

            return res;

        }catch (Exception ex)
        {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
    public ResponseDto descargarDocumentoAdjunto(Long  pDocumentoAdjuntoId){
        ResponseDto res = new ResponseDto();
        try{

            Optional<DocumentoAdjuntoEntity> obj =  iDocumentoAdjuntoDao.findById(pDocumentoAdjuntoId);
            byte[] dataByte = Base64.getDecoder().decode(obj.get().getArchivo());

            ResponseEntity<byte[]> responseEntity =  ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + obj.get().getNombreArchivo()+"."+obj.get().getExtensionArchivo())
                    // Content-Type
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    // Contet-Length
                    .contentLength(dataByte.length) //
                    .body(dataByte);


            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(responseEntity);
            return res;

        }catch (Exception ex)
        {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
}
