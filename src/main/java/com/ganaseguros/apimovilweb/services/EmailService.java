package com.ganaseguros.apimovilweb.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public void sendEmail(String emailTo){
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject("Listado");
            helper.setText("texo de prueba");
            javaMailSender.send(message);


        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void enviarCorreoHtml(String pDestino, String pAsunto, String pMensaje) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            List<InternetAddress> vListPara = new ArrayList<>();
            vListPara.add(new InternetAddress(pDestino));


           // InternetAddress[] vDestinosArray = InternetAddress.parse(vListPara.toString(), true);

            helper.setFrom(email);
            helper.setTo(pDestino);

            helper.setSubject(pAsunto);
            helper.setText(pMensaje, true);
            javaMailSender.send(message);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }




}
