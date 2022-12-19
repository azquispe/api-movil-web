package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dao.IAvisoDao;
import com.ganaseguros.apimovilweb.domain.dto.AvisoDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.entity.AvisoEntity;
import com.ganaseguros.apimovilweb.entity.OfertaSliderEntity;
import com.ganaseguros.apimovilweb.utils.FuncionesFechas;
import com.ganaseguros.apimovilweb.utils.FuncionesGenerales;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AvisoService {
    @Autowired
    private IAvisoDao iAvisoDao;

    //final String baseUrl = "https://fcm.googleapis.com";

    @Value("${url.firebase}")
    private String baseUrl;


    public ResponseDto obtenerAvisosTodos (){
        ResponseDto res = new ResponseDto();
        try{
            List<AvisoEntity> lstAvisoEntity = iAvisoDao.obtenerAvisosTodos();
            List<AvisoDto> lstDto = new ArrayList<>();
            AvisoDto objDto = null;
            for (AvisoEntity obj :lstAvisoEntity) {
                objDto = new AvisoDto();
                objDto.setAvisoId(obj.getAvisoId());
                objDto.setTitulo(obj.getTitulo());
                objDto.setContenido(obj.getContenido());
                objDto.setFechaAviso(obj.getFechaAviso()!=null?FuncionesFechas.ConvertirDateToString(obj.getFechaAviso()):"" );
                objDto.setEnlace(obj.getEnlace());
                objDto.setAplicacionId(obj.getAplicacionId()); // 1002 web    1003 movil
                lstDto.add(objDto);
            }
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(lstDto);
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
    public ResponseDto obtenerAvisosPorAplicacionID (Long pAplicacionID){
        ResponseDto res = new ResponseDto();
        try{
            List<AvisoEntity> lstAvisoEntity = iAvisoDao.obtenerAvisosPorAplicacionID(pAplicacionID);
            List<AvisoDto> lstDto = new ArrayList<>();
            AvisoDto objDto = null;
            for (AvisoEntity obj :lstAvisoEntity) {
                objDto = new AvisoDto();
                objDto.setAvisoId(obj.getAvisoId());
                objDto.setTitulo(obj.getTitulo());
                objDto.setContenido(obj.getContenido());
                objDto.setFechaAviso(obj.getFechaAviso()!=null?FuncionesFechas.ConvertirDateToString(obj.getFechaAviso()):"" );
                objDto.setEnlace(obj.getEnlace());
                objDto.setAplicacionId(obj.getAplicacionId()); // 1002 web    1003 movil
                lstDto.add(objDto);
            }
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(lstDto);
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
    public ResponseDto insertAviso(AvisoDto pAvisoDto){
        ResponseDto res = new ResponseDto();
        try{
            AvisoEntity insert = new AvisoEntity();
            insert.setTitulo(pAvisoDto.getTitulo());
            insert.setContenido(pAvisoDto.getContenido());
            insert.setEnlace(pAvisoDto.getEnlace());
            insert.setAplicacionId(pAvisoDto.getAplicacionId());
            insert.setFechaAviso(new Date());
            insert.setFechaRegistro(new Date());
            insert.setEstadoId(ConstEstado.ACTIVO);
            iAvisoDao.save(insert);
            if(pAvisoDto.getAplicacionId().longValue()==1003l)
                FuncionesGenerales.enviaPushAviso("AVISO","Nuevo Aviso Registrado",baseUrl);

            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);

            return res;

        }catch (Exception ex)
        {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
// prueba
    public ResponseDto eliminarAvisoPorID (Long pAvisoId){
        ResponseDto res = new ResponseDto();
        try{
            Optional<AvisoEntity> objAvisoEntity = iAvisoDao.obtenerAvisoPorID(pAvisoId);
            if(objAvisoEntity.isPresent()){
                AvisoEntity update = objAvisoEntity.get();
                update.setEstadoId(ConstEstado.ANULADO);
                iAvisoDao.save(update);
                res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
                res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            }else{
                res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            }
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
}
