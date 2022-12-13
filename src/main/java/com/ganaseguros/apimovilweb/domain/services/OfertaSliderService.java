package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dao.IOfertaSliderDao;
import com.ganaseguros.apimovilweb.domain.dto.OfertaSliderDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.entity.OfertaSliderEntity;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OfertaSliderService {

    @Autowired
    private IOfertaSliderDao iOfertaSliderDao;


    public ResponseDto insertOfertaSlider(OfertaSliderDto pOfertaSliderDto){
        ResponseDto res = new ResponseDto();
        try{
            OfertaSliderEntity insert = new OfertaSliderEntity();
            insert.setTitulo(pOfertaSliderDto.getTitulo());
            insert.setContenido(pOfertaSliderDto.getContenido());
            insert.setEnlace(pOfertaSliderDto.getEnlace());
            insert.setAplicacionId(pOfertaSliderDto.getAplicacionId());
            insert.setFechaRegistro(new Date());
            insert.setEstadoId(ConstEstado.ACTIVO);

            iOfertaSliderDao.save(insert);
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
    public ResponseDto obtenerAvisosPorAplicacionID (Long pAplicacionID){
        ResponseDto res = new ResponseDto();
        try{
            List<OfertaSliderEntity> lstOfertaEntity = iOfertaSliderDao.obtenerAvisosPorAplicacionID(pAplicacionID);
            List<OfertaSliderDto> lstDto = new ArrayList<>();
            OfertaSliderDto objDto = null;
            for (OfertaSliderEntity obj :lstOfertaEntity) {
                objDto = new OfertaSliderDto();
                objDto.setOfertaSliderId(obj.getOfertaSliderId());
                objDto.setTitulo(obj.getTitulo());
                objDto.setContenido(obj.getContenido());
                objDto.setEnlace(obj.getEnlace());
                objDto.setAplicacionId(obj.getAplicacionId());
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
}
