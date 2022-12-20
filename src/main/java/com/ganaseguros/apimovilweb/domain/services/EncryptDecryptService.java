package com.ganaseguros.apimovilweb.domain.services;


import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.utils.FuncionesGenericos;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.util.Base64;

@Service
public class EncryptDecryptService  {

    //referencia
    //https://gist.github.com/jsgao0/52ca6f835a00cccb3cef164f2b9035c1


    // de momento este metodo "encryptMessage" no se usa, ya que el front con la llave publica lo cifra

    public ResponseDto encryptMessage(String plainText) {
        ResponseDto response = new ResponseDto();
        try {
            byte[] contentBytes = plainText.getBytes();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, FuncionesGenericos.readPublicKey());
            byte[] cipherContent = cipher.doFinal(contentBytes);
            String encoded = Base64.getEncoder().encodeToString(cipherContent);
            response.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            response.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            response.setElementoGenerico(encoded);

        } catch (Exception e) {
            response.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            response.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return response;
    }


    public ResponseDto decryptMessage(String encryptedMessgae) {
        ResponseDto response = new ResponseDto();
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, FuncionesGenericos.readPrivateKey());
            byte[] cipherContentBytes = Base64.getMimeDecoder().decode(encryptedMessgae.getBytes());
            byte[] decryptedContent = cipher.doFinal(cipherContentBytes);
            String decoded = new String(decryptedContent);
            response.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            response.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            response.setElementoGenerico(decoded);

        } catch (Exception e) {
            response.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            response.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return response;

    }


}
