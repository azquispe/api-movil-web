package com.ganaseguros.apimovilweb.services;

import com.ganaseguros.apimovilweb.dao.IDominioDao;
import com.ganaseguros.apimovilweb.dto.DominioDto;
import com.ganaseguros.apimovilweb.dto.ResponseDto;
import com.ganaseguros.apimovilweb.entity.DominioEntity;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DominioService {
    @Autowired
    private IDominioDao iDominioDao;


    public ResponseDto findByDominio(String pDominio) {
        ResponseDto res = new ResponseDto();
        try{
            DominioDto dominioDto = null;
            List<DominioDto> lstDominioDto = new ArrayList<>();
            List<DominioEntity> lstDominioEntity =  this.iDominioDao.findByDominio(pDominio);
            for (DominioEntity dom :lstDominioEntity) {
                dominioDto = new DominioDto();
                dominioDto.setDominioId(dom.getDominioId());
                dominioDto.setDominio(dom.getDominio());
                dominioDto.setAbreviatura(dom.getAbreviatura());
                dominioDto.setDescripcion(dom.getDescripcion());
                lstDominioDto.add(dominioDto);
            }
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(lstDominioDto);
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;

    }

}
