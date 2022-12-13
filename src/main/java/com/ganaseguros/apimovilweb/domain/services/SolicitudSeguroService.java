package com.ganaseguros.apimovilweb.domain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganaseguros.apimovilweb.domain.dao.IDominioDao;
import com.ganaseguros.apimovilweb.domain.dao.ISolicitudSeguroDao;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.domain.dto.SolicitudSeguroDto;
import com.ganaseguros.apimovilweb.entity.SolicitudSeguroEntity;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstEstado;
import com.ganaseguros.apimovilweb.utils.constantes.ConstAplicacion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
@RequiredArgsConstructor
public class SolicitudSeguroService {

    private final RestTemplate restTemplate;
    //final String baseUrl = "https://sociedadganadero--devtemp8.sandbox.my.salesforce.com";


    ObjectMapper oMapper = new ObjectMapper();

    @Autowired
    private EmailService emailService;

    @Autowired
    private ISolicitudSeguroDao iSolicitudSeguroDao;

    @Autowired
    private IDominioDao iDominioDao;




    public ResponseDto enviarSolicitudSeguro(SolicitudSeguroDto pSolicitudSeguroDto) {
        ResponseDto res = new ResponseDto();
        try {
            if (pSolicitudSeguroDto.getAplicacionId() == null || pSolicitudSeguroDto.getAplicacionId() <= 0) {
                res.setCodigo(ConstDiccionarioMensaje.CODMW2006);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2006_MENSAJE);
                return res;
            }
            if (
                    (pSolicitudSeguroDto.getNombres() == null || pSolicitudSeguroDto.getNombres().trim().equals("")) ||
                            (pSolicitudSeguroDto.getApellidos() == null || pSolicitudSeguroDto.getApellidos().trim().equals("")) ||
                            (pSolicitudSeguroDto.getTelefonoCelular() == null || pSolicitudSeguroDto.getTelefonoCelular().trim().equals("")) ||
                            (pSolicitudSeguroDto.getCiudad() == null || pSolicitudSeguroDto.getCiudad().trim().equals(""))
            ) {

                res.setCodigo(ConstDiccionarioMensaje.CODMW2004);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2004_MENSAJE);
                return res;

            }

            String medio = "";
            if (pSolicitudSeguroDto.getAplicacionId().longValue() == ConstAplicacion.MOVIL.longValue())
                medio = "Móvil";
            if (pSolicitudSeguroDto.getAplicacionId().longValue()==ConstAplicacion.WEB.longValue())
                medio = "Web";

            // Crear el asunto
            String vAsunto = "SOLICITUD DE SEGURO";

            // Crear el cuerpo del correo
            StringBuilder vBody = new StringBuilder();




            vBody.append("<div style='background-color: #6fbf31; color: #fff; width: 100%; height: 50px; font-size: 40px; text-align: center;'>SOLICITUD DE SEGURO</div>");
            vBody.append("<p>Mediante la presente se informa de una solicitud de Seguro desde la Aplicaci&oacute;n "+medio+" con el siguiente detalle:</p>");
            vBody.append("<table style='border-collapse: collapse; width: 100%; height: 126px;' border='1'>");
            vBody.append("<tbody>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Nombre(s)</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudSeguroDto.getNombres() != null ? pSolicitudSeguroDto.getNombres() : " - " + "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Apellido(s)</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudSeguroDto.getApellidos() != null ? pSolicitudSeguroDto.getApellidos() : " - " + "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Teléfono o Celular</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudSeguroDto.getTelefonoCelular() != null ? pSolicitudSeguroDto.getTelefonoCelular() : "" + "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Correo</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudSeguroDto.getCorreo() != null ? pSolicitudSeguroDto.getCorreo() : "" + "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Ciudad</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudSeguroDto.getCiudad() != null ? pSolicitudSeguroDto.getCiudad() : ""+  "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Tiene algun seguro contratado con nosotros?</strong></td>");
            if (pSolicitudSeguroDto.isTieneSeguroNosotros()) {
                vBody.append("<td style='width: 50%; height: 18px;'>SI</td>");
            } else {
                vBody.append("<td style='width: 50%; height: 18px;'>NO</td>");
            }
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Tiene algun seguro contratado con otra compañia?</strong></td>");
            if (pSolicitudSeguroDto.isTieneSeguroOtros()) {
                vBody.append("<td style='width: 50%; height: 18px;'>SI</td>");
            } else {
                vBody.append("<td style='width: 50%; height: 18px;'>NO</td>");
            }
            vBody.append("</tr>");

            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Descripción de la solicitud</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudSeguroDto.getDescripcion () != null ?  pSolicitudSeguroDto.getDescripcion ()  : "" + "</td>");
            vBody.append("</tr>");

            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Tipo de seguro de interés</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudSeguroDto.getTipoProductoId () != null ? iDominioDao.getDominioByDominioId(  pSolicitudSeguroDto.getTipoProductoId()).get().getDescripcion()  : "" + "</td>");
            vBody.append("</tr>");

            vBody.append("</tbody>");
            vBody.append("</table>");
            vBody.append("<p></p>");
            vBody.append("<hr />");

            vBody.append("<p><strong>GANASEGUROS</strong><br />Correo generado desde la Aplicaci&oacute;n " + medio + ".</p>");
            vBody.append(" <img src='https://front-funcionales.azurewebsites.net/img/logo_ganaseguros3.c585e0d6.jpg' > ");


            // Determinar el destinatario copia
            //String vDestino = "azquispe@bg.com.bo";
            String vDestino = "alvaroquispesegales@gmail.com";
            //String vDestino = "alvaro20092004@hotmail.com";

            try {
                SolicitudSeguroEntity objInsert = new SolicitudSeguroEntity();
                objInsert.setNombres(pSolicitudSeguroDto.getNombres());
                objInsert.setApellidos(pSolicitudSeguroDto.getApellidos());
                objInsert.setTelefonoCelular(pSolicitudSeguroDto.getTelefonoCelular());
                objInsert.setCiudad(pSolicitudSeguroDto.getCiudad());
                objInsert.setTipoProductoId(pSolicitudSeguroDto.getTipoProductoId());
                objInsert.setCorreo(pSolicitudSeguroDto.getCorreo());
                objInsert.setTieneSeguroNosotros(pSolicitudSeguroDto.isTieneSeguroNosotros());
                objInsert.setTieneSeguroOtros(pSolicitudSeguroDto.isTieneSeguroOtros());
                objInsert.setCreadoCrm(false);
                objInsert.setDescripcion(pSolicitudSeguroDto.getDescripcion());
                objInsert.setAplicacionId(pSolicitudSeguroDto.getAplicacionId());
                objInsert.setFechaRegistro(new Date());
                objInsert.setEstadoId(ConstEstado.ACTIVO);
                iSolicitudSeguroDao.save(objInsert);

                emailService.enviarCorreoHtml(vDestino, vAsunto, vBody.toString());

            } catch (Exception ex) {
                //... insertar log
            }
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            return res;


        } catch (Exception ex) {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            return res;
        }
    }


}
