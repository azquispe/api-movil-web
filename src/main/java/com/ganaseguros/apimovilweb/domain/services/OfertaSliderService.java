package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dao.IOfertaSliderDao;
import com.ganaseguros.apimovilweb.domain.dto.OfertaSliderDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.entity.OfertaSliderEntity;
import com.ganaseguros.apimovilweb.utils.FuncionesGenericos;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OfertaSliderService {

    @Value("${url.firebase}")
    private String baseUrl;
    @Autowired
    private IOfertaSliderDao iOfertaSliderDao;


    public ResponseDto insertOfertaSlider(OfertaSliderDto pOfertaSliderDto){
        ResponseDto res = new ResponseDto();
        try{
            OfertaSliderEntity insert = new OfertaSliderEntity();
            insert.setTitulo(pOfertaSliderDto.getTitulo());
            insert.setSubtitulo(pOfertaSliderDto.getSubtitulo());
            insert.setContenido(pOfertaSliderDto.getContenido());
            insert.setAplicacionId(pOfertaSliderDto.getAplicacionId());
            insert.setDocumentoAdjuntoId(pOfertaSliderDto.getDocumentoAdjuntoId());
            insert.setFechaRegistro(new Date());
            insert.setEstadoId(ConstEstado.ACTIVO);
            iOfertaSliderDao.save(insert);
            if(pOfertaSliderDto.getAplicacionId().longValue()==1003l)
                FuncionesGenericos.enviaPushAviso("OFERTA","Nuevo Oferta Registrado",baseUrl);

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
    public ResponseDto obtenerOfertasPorAplicacionID (Long pAplicacionID){
        ResponseDto res = new ResponseDto();
        try{
            List<OfertaSliderEntity> lstOfertaEntity = iOfertaSliderDao.obtenerOfertasPorAplicacionID(pAplicacionID);
            List<OfertaSliderDto> lstDto = new ArrayList<>();
            OfertaSliderDto objDto = null;
            for (OfertaSliderEntity obj :lstOfertaEntity) {
                objDto = new OfertaSliderDto();
                objDto.setOfertaSliderId(obj.getOfertaSliderId());
                objDto.setTitulo(obj.getTitulo());
                objDto.setSubtitulo(obj.getSubtitulo());
                objDto.setContenido(obj.getContenido());
                objDto.setAplicacionId(obj.getAplicacionId());
                objDto.setDocumentoAdjuntoId(obj.getDocumentoAdjuntoId());
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
    public ResponseDto obtenerOfertasTodos (){
        ResponseDto res = new ResponseDto();
        try{
            List<OfertaSliderEntity> lstOfertaEntity = iOfertaSliderDao.obtenerOfertasTodos();
            List<OfertaSliderDto> lstDto = new ArrayList<>();
            OfertaSliderDto objDto = null;
            for (OfertaSliderEntity obj :lstOfertaEntity) {
                objDto = new OfertaSliderDto();
                objDto.setOfertaSliderId(obj.getOfertaSliderId());
                objDto.setTitulo(obj.getTitulo());
                objDto.setSubtitulo(obj.getSubtitulo());
                objDto.setContenido(obj.getContenido());
                objDto.setAplicacionId(obj.getAplicacionId());
                objDto.setDocumentoAdjuntoId(obj.getDocumentoAdjuntoId());
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
    public ResponseDto eliminarOfertasPorID (Long pOfertaSliderId){
        ResponseDto res = new ResponseDto();
        try{
            Optional<OfertaSliderEntity> objOfertaEntity = iOfertaSliderDao.obtenerOfertasPorID(pOfertaSliderId);
            if(objOfertaEntity.isPresent()){
                OfertaSliderEntity update = objOfertaEntity.get();
                update.setEstadoId(ConstEstado.ANULADO);
                iOfertaSliderDao.save(update);
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
