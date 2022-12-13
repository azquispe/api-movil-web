package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dao.IAvisoDao;
import com.ganaseguros.apimovilweb.domain.dto.AvisoDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.entity.AvisoEntity;
import com.ganaseguros.apimovilweb.utils.FuncionesFechas;
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

            this.enviaPushAviso("AVISO","Nuevo Aviso Registrado");

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
    public ResponseDto enviaPushAviso (String title, String text){
        ResponseDto res = new ResponseDto();
        try{
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("body", text);

            Map<String, Object> data = new HashMap<>();
            data.put("badge", "1");
            data.put("click_action", "FLUTTER_NOTIFICATION_CLICK");
            data.put("id", "notificacion_aviso");
            data.put("status", "done");

            Map<String, Object> body = new HashMap<>();
            body.put("to", "/topics/anuncios_ganaseguros_2022_dev");
            body.put("notification", notification);
            body.put("priority", "high");
            body.put("data", data);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Key=AAAASRULFRc:APA91bER6GLzPHEIvMBcgkwVitC1d66ByehZbWgWLG6dRkIF8zLuAHz81ZsLfvRhnJF4oakl7PjJAqY8Eca-ngTavcbKt4prXSGfGyVfemPh9M9LGQDUS8Xx7sCXF3N6uRHb9FTZXj7R ");

            HttpEntity<Map> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> resultMap = restTemplate.postForEntity(baseUrl+"/fcm/send", request, Map.class);

            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);

        }catch (Exception ex){

            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);


        }
        return  res;
    }
}
