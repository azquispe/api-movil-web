package com.ganaseguros.apimovilweb.utils;

import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class FuncionesGenerales {
    public static ResponseDto enviaPushAviso (String title, String text, String baseUrl){
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
