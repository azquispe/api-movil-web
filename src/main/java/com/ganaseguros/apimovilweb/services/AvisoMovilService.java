package com.ganaseguros.apimovilweb.services;

import com.ganaseguros.apimovilweb.dao.IAvisoMovilDao;
import com.ganaseguros.apimovilweb.dao.ISolicitudSeguroDao;
import com.ganaseguros.apimovilweb.dto.AvisoMovilDto;
import com.ganaseguros.apimovilweb.dto.ResponseDto;
import com.ganaseguros.apimovilweb.entity.AvisoMovilEntity;
import com.ganaseguros.apimovilweb.utils.FuncionesFechas;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AvisoMovilService {
    @Autowired
    private IAvisoMovilDao iAvisoMovilDao;

    public ResponseDto findAll (){
        ResponseDto res = new ResponseDto();
        try{
            List<AvisoMovilEntity> lstAvisoEntity = iAvisoMovilDao.findAll();
            List<AvisoMovilDto> lstDto = new ArrayList<>();
            AvisoMovilDto objDto = null;
            for (AvisoMovilEntity obj :lstAvisoEntity) {
                objDto = new AvisoMovilDto();
                objDto.setAvisosMovilId(obj.getAvisosMovilId());
                objDto.setTitulo(obj.getTitulo());
                objDto.setContenido(obj.getContenido());
                objDto.setFechaAviso(obj.getFechaAviso()!=null?FuncionesFechas.ConvertirDateToString(obj.getFechaAviso()):"" );
                objDto.setRuta(obj.getRuta());
                objDto.setEnlace(obj.getEnlace());
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
    public ResponseDto insertAvisoMovil(AvisoMovilDto avisoMovilDto){
        ResponseDto res = new ResponseDto();
        try{
            AvisoMovilEntity insert = new AvisoMovilEntity();
            insert.setTitulo(avisoMovilDto.getTitulo());
            insert.setContenido(avisoMovilDto.getContenido());
            insert.setEnlace(avisoMovilDto.getEnlace());
            insert.setRuta(avisoMovilDto.getRuta());
            insert.setFechaAviso(new Date());
            insert.setFechaRegistro(new Date());
            insert.setEstadoId(ConstEstado.ACTIVO);

            iAvisoMovilDao.save(insert);

            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);

        }catch (Exception ex)
        {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
}
