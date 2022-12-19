package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dao.IPersonaDao;
import com.ganaseguros.apimovilweb.domain.dto.AvisoDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.dto.UsuarioPersonaDto;
import com.ganaseguros.apimovilweb.entity.AvisoEntity;
import com.ganaseguros.apimovilweb.entity.PersonaEntity;
import com.ganaseguros.apimovilweb.utils.FuncionesFechas;
import com.ganaseguros.apimovilweb.utils.FuncionesGenerales;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstEstado;

import java.util.Date;

public class UsuarioService {
    private IPersonaDao  iPersonaDao;

    public ResponseDto insertarUsuarioYpersona(UsuarioPersonaDto pUsuarioPersonaDto ) {
        ResponseDto res = new ResponseDto();
        try {
            PersonaEntity insert = new PersonaEntity();
            insert.setGeneroId(pUsuarioPersonaDto.getGeneroId());
            insert.setNombres(pUsuarioPersonaDto.getNombres());
            insert.setApellidoPaterno(pUsuarioPersonaDto.getApellidoPaterno());
            insert.setApellidoMaterno(pUsuarioPersonaDto.getApellidoMaterno());
            insert.setApellidoEsposo(pUsuarioPersonaDto.getApellidoEsposo());
            insert.setNumeroDocumento(pUsuarioPersonaDto.getNumeroDocumento());
            insert.setComplemento(pUsuarioPersonaDto.getComplemento());
            insert.setCiudadExpedidoId(pUsuarioPersonaDto.getCiudadExpedidoId());
            insert.setNumeroCelular(pUsuarioPersonaDto.getNumeroCelular());
            insert.setCorreoElectronico(pUsuarioPersonaDto.getCorreoElectronico());
            insert.setFechaNacimiento(FuncionesFechas.ConvertirStringToDate(pUsuarioPersonaDto.getFechaNacimiento()));
            insert.setFechaRegistro(new Date());
            insert.setEstadoId(ConstEstado.ACTIVO);
            iPersonaDao.save(insert);

            // falta crear usuario

            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);

            return res;

        } catch (Exception ex) {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
}
