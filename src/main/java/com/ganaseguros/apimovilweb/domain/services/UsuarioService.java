package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dao.IPersonaDao;
import com.ganaseguros.apimovilweb.domain.dao.IUsuarioDao;
import com.ganaseguros.apimovilweb.domain.dto.PersonaDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.dto.UsuarioDto;
import com.ganaseguros.apimovilweb.domain.dto.UsuarioPersonaDto;
import com.ganaseguros.apimovilweb.entity.PersonaEntity;
import com.ganaseguros.apimovilweb.entity.UsuarioEntity;
import com.ganaseguros.apimovilweb.utils.FuncionesFechas;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private IPersonaDao  iPersonaDao;
    @Autowired
    private IUsuarioDao iUsuarioDao;

    public ResponseDto insertarUsuarioPersona(UsuarioPersonaDto pUsuarioPersonaDto ) {
        ResponseDto res = new ResponseDto();
        PersonaDto personaDto = pUsuarioPersonaDto.getPersonaDto();
        UsuarioDto usuarioDto = pUsuarioPersonaDto.getUsuarioDto();
        try {

            Optional<UsuarioEntity>  objUsuario = iUsuarioDao.obtenerUsuarioPorLogin(pUsuarioPersonaDto.getUsuarioDto().getLogin());
            if(objUsuario.isPresent()){
                res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
                return res;
            }

            PersonaEntity insert = new PersonaEntity();
            insert.setGeneroId(personaDto.getGeneroId());
            insert.setNombres(personaDto.getNombres());
            insert.setApellidoPaterno(personaDto.getApellidoPaterno());
            insert.setApellidoMaterno(personaDto.getApellidoMaterno());
            insert.setApellidoEsposo(personaDto.getApellidoEsposo());
            insert.setNumeroDocumento(personaDto.getNumeroDocumento());
            insert.setComplemento(personaDto.getComplemento());
            insert.setCiudadExpedidoId(personaDto.getCiudadExpedidoId());
            insert.setNumeroCelular(personaDto.getNumeroCelular());
            insert.setCorreoElectronico(personaDto.getCorreoElectronico());
            insert.setFechaNacimiento(FuncionesFechas.ConvertirStringToDate(personaDto.getFechaNacimiento()));
            insert.setFechaRegistro(new Date());
            insert.setEstadoId(ConstEstado.ACTIVO);
            iPersonaDao.save(insert);


            UsuarioEntity insertU = new UsuarioEntity();
            insertU.setPersonaId(insert.getPersonaId());
            insertU.setAplicacionId(usuarioDto.getAplicacionId());
            insertU.setTipoUsuarioId(usuarioDto.getTipoUsuarioId());
            insertU.setLogin(usuarioDto.getLogin());
            insertU.setPassword(usuarioDto.getPassword());
            insertU.setFechaInicio(new Date());
            insertU.setFechaRegistro(new Date());
            insertU.setEstadoId(ConstEstado.ACTIVO);
            iUsuarioDao.save(insertU);


            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);

            return res;

        } catch (Exception ex) {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
    public ResponseDto actualizarUsuarioPersona(PersonaDto pPersonaDto){
        ResponseDto res = new ResponseDto();

        try{
            Optional<PersonaEntity> objPersona  =  iPersonaDao.obtenerPersonaPorID(pPersonaDto.getPersonaId());
            Optional<UsuarioEntity> objUsuario = iUsuarioDao.obtenerUsuarioPorPersonaId(pPersonaDto.getPersonaId());

            if(!objPersona.isPresent() || !objUsuario.isPresent()){
                res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
                res.setCodigo(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
                return res;
            }
            PersonaEntity updatePersona = objPersona.get();
            UsuarioEntity updateUsuario = objUsuario.get();

            updatePersona.setGeneroId(pPersonaDto.getGeneroId());
            updatePersona.setNombres(pPersonaDto.getNombres());
            updatePersona.setApellidoPaterno(pPersonaDto.getApellidoPaterno());
            updatePersona.setApellidoMaterno(pPersonaDto.getApellidoMaterno());
            updatePersona.setNumeroDocumento(pPersonaDto.getNumeroDocumento());
            updatePersona.setCiudadExpedidoId(pPersonaDto.getCiudadExpedidoId());
            updatePersona.setComplemento(pPersonaDto.getComplemento());
            updatePersona.setNumeroCelular(pPersonaDto.getNumeroCelular());
            updatePersona.setCorreoElectronico(pPersonaDto.getCorreoElectronico());
            updatePersona.setFechaNacimiento(FuncionesFechas.ConvertirStringToDate(pPersonaDto.getFechaNacimiento()));
            iPersonaDao.save(updatePersona);

            updateUsuario.setLogin(pPersonaDto.getNumeroDocumento());
            iUsuarioDao.save(updateUsuario);


            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            return res;
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            // seria ideal registrar log en BD
            return res;
        }
    }
    public ResponseDto autentication(Map objRequest ) {
        ResponseDto res = new ResponseDto();
        try{
            String vLogin = objRequest.get("login") != null ? objRequest.get("login").toString() : "";
            String vPassword = objRequest.get("password") != null ? objRequest.get("password").toString() : "";
            if(vLogin.isEmpty() || vPassword.isEmpty()){
                res.setCodigo(ConstDiccionarioMensaje.CODMW2008);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2008_MENSAJE);
                return res;
            }
            Optional<UsuarioEntity> vUsuarioEntity =  iUsuarioDao.obtenerUsuarioPorLogin(vLogin);
            if(vUsuarioEntity.isEmpty() || !vUsuarioEntity.get().getPassword().equals(vPassword)){
                res.setCodigo(ConstDiccionarioMensaje.CODMW2007);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2007_MENSAJE);
                return res;
            }
            Optional<PersonaEntity> personaEntity =  iPersonaDao.obtenerPersonaPorID(vUsuarioEntity.get().getPersonaId());
            if(personaEntity.isEmpty()){
                res.setCodigo(ConstDiccionarioMensaje.CODMW2007);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2007_MENSAJE);
                return res;
            }
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(PersonaEntityConvertToDto(personaEntity.get()));
            return res;
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            return res;
        }

    }

    public ResponseDto obtenerPersonaPorId(Long  pPersonaId ) {
        ResponseDto res = new ResponseDto();
        try{
           if(pPersonaId<=0){
               res.setCodigo(ConstDiccionarioMensaje.CODMW2007);
               res.setMensaje(ConstDiccionarioMensaje.CODMW2007_MENSAJE);
               return res;
           }
           Optional<PersonaEntity> objPersona =  iPersonaDao.obtenerPersonaPorID(pPersonaId);
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(PersonaEntityConvertToDto(objPersona.get()));
            return res;
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            return res;
        }

    }
    private PersonaDto PersonaEntityConvertToDto(PersonaEntity pPersonaEntity){
        PersonaDto dto = new PersonaDto();
        dto.setPersonaId(pPersonaEntity.getPersonaId());
        dto.setGeneroId(pPersonaEntity.getGeneroId());
        dto.setNombres(pPersonaEntity.getNombres());
        dto.setApellidoPaterno(pPersonaEntity.getApellidoPaterno());
        dto.setApellidoMaterno(pPersonaEntity.getApellidoMaterno());
        dto.setNumeroDocumento(pPersonaEntity.getNumeroDocumento());
        dto.setComplemento(pPersonaEntity.getComplemento());
        dto.setCiudadExpedidoId(pPersonaEntity.getCiudadExpedidoId());
        dto.setNumeroCelular(pPersonaEntity.getNumeroCelular());
        dto.setCorreoElectronico(pPersonaEntity.getCorreoElectronico());
        dto.setFechaNacimiento(FuncionesFechas.ConvertirDateToString(pPersonaEntity.getFechaNacimiento()));
        return dto;

    }
}
