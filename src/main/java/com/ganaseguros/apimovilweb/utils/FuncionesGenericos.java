package com.ganaseguros.apimovilweb.utils;

import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FuncionesGenericos {
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
    public static String pdfToBase64(String pPathFile) {
        try {
            byte[] input_file = Files.readAllBytes(Paths.get(pPathFile));
            byte[] encodedBytes = Base64.getEncoder().encode(input_file);
            return  new String(encodedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey readPublicKey() throws Exception {
        String publicKeyPEM = RSA_for_PASSWORD.RSA_PUBLIC
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");
        byte[] keyBytes = Base64.getMimeDecoder().decode(publicKeyPEM.getBytes());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(spec);
        return key;
    }

    public static PrivateKey readPrivateKey() throws Exception {
        String privateKeyPEM = RSA_for_PASSWORD.RSA_PRIVATE
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] keyBytes = Base64.getMimeDecoder().decode(privateKeyPEM.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        return key;

    }
}
