package com.ganaseguros.apimovilweb.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganaseguros.apimovilweb.dao.IDominioDao;
import com.ganaseguros.apimovilweb.dao.ISolicitudSeguroDao;
import com.ganaseguros.apimovilweb.dto.PolizaDto;
import com.ganaseguros.apimovilweb.dto.ResponseDto;
import com.ganaseguros.apimovilweb.dto.SolicitudPolizaDto;
import com.ganaseguros.apimovilweb.entity.SolicitudSeguroEntity;
import com.ganaseguros.apimovilweb.utils.FuncionesFechas;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
import com.ganaseguros.apimovilweb.utils.constantes.ConstTipoMedioSolicitudSeguro;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ConsultaPolizasService {

    private final RestTemplate restTemplate;
    //final String baseUrl = "https://sociedadganadero--devtemp8.sandbox.my.salesforce.com";

    @Value("${url.salesforce}")
    private String urlSalesforce;

    ObjectMapper oMapper = new ObjectMapper();

    @Autowired
    private EmailService emailService;

    @Autowired
    private ISolicitudSeguroDao iSolicitudSeguroDao;

    @Autowired
    private IDominioDao iDominioDao;


    public String obtenerToken() throws URISyntaxException {
        ResponseEntity<Map> resultMap = restTemplate.postForEntity(new URI(urlSalesforce + "/services/oauth2/token?grant_type=password&" +
                "client_id=3MVG99VEEJ_Bj3.7I7xGG.Bq_J8F3a4MeRx2mQ_YyzMsOkrBBLD_brf0BO42IQUn2rtDSdv5ryAoCQLuDZcwP&" +
                "client_secret=77ADEFF8025EEBE56BC9DBA7AC5A45685293C40B2A553F1A4B35DCC0CE454E68" +
                "&username=ararancibia@bg.com.bo.devtemp8&password=fa3BO2aa77cPSdT836OF12yYKHE2fj7Y"), new HashMap<>(), Map.class);
        if (resultMap != null && resultMap.getStatusCode().value() == 200) {
            return resultMap.getBody().get("access_token").toString();
        } else {
            return null;
        }
    }

    public ResponseDto consultaPoliza(Map objRequest) {

        String vNroDocumento = objRequest.get("nroDocumento") != null ? objRequest.get("nroDocumento").toString() : "";
        String vCiudadExpedido = objRequest.get("ciudadExpedido") != null ? objRequest.get("ciudadExpedido").toString() : ""; // de momento esto no se utiliza, el Servicio de Abel no usa
        String vFechaNac = objRequest.get("fechaNacimiento") != null ? objRequest.get("fechaNacimiento").toString() : "";
        String vComplemento = objRequest.get("complemento") != null ? objRequest.get("complemento").toString() : "";


        Map<String, Object> mapDatosPersona = null;
        Map<String, Object> mapDatosPoliza = null;
        Map<String, Object> mapDatosPolizaDetalle = null;

        List<PolizaDto> lstPolizas = new ArrayList<>();
        ResponseDto res = new ResponseDto();

        try {

            if (vNroDocumento.trim().equals("") || vFechaNac.trim().equals("")) {
                res.setCodigo(ConstDiccionarioMensaje.CODMW2001);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2001_MENSAJE);
                return res;
            }

            String token = this.obtenerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            // BUSCA DATOS PERSONA
            //  Consulta Datos Persona por Documento de Identidad
            // https://ap1.salesforce.com/services/apexrest/vlocity_ins/v1/integrationprocedure/bg_ti_g_getPersonInformation


            String urlDatosPersona = urlSalesforce + "/services/apexrest/vlocity_ins/v1/integrationprocedure/" + "bg_ti_g_getPersonInformation?searchCriteria=1&identificationType=CI&identificationNumber=" + vNroDocumento + "&identificationPlugin=" + vComplemento;
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<String> resultMapDatosPersona = restTemplate.exchange(
                    urlDatosPersona,
                    HttpMethod.GET,
                    request,
                    String.class,
                    1
            );
            if (resultMapDatosPersona != null && resultMapDatosPersona.getStatusCode().value() == 200) {
                mapDatosPersona = new ObjectMapper().readValue(resultMapDatosPersona.getBody(), Map.class);
                if (mapDatosPersona.get("codigoMensaje").equals("CODSF1000") && mapDatosPersona.get("Customers") != null) {

                    Map<String, Object> objCustomer = oMapper.convertValue(mapDatosPersona.get("Customers"), Map.class);
                    List<Map<String, Object>> lstPersonas = oMapper.convertValue(objCustomer.get("objetos"), ArrayList.class);

                    //BUSCA PÓLIZAS
                    //  Consulta Póliza(s) de una Persona por “AccountId”
                    //  https://ap1.salesforce.com/services/apexrest/vlocity_ins/v1/integrationprocedure/bg_ti_g_getProductInformation
                    for (Map<String, Object> vAccountIdMap : lstPersonas) {

                        String urlPolizas = urlSalesforce + "/services/apexrest/vlocity_ins/v1/integrationprocedure/bg_ti_g_getProductInformation";
                        Map<String, Object> requestGetPoliza = new HashMap<>();
                        requestGetPoliza.put("AccountId", vAccountIdMap.get("AccountId").toString());
                        if (vAccountIdMap.get("CustomerBirthDate") == null || !FuncionesFechas.formatearFecha_ddmmyyyy(vAccountIdMap.get("CustomerBirthDate") + "").equals(vFechaNac)) {
                            res.setCodigo(ConstDiccionarioMensaje.CODMW2002);
                            res.setMensaje(ConstDiccionarioMensaje.CODMW2002_MENSAJE);
                            return res;
                        }


                        HttpEntity<Map> bodyGetPoliza = new HttpEntity<>(requestGetPoliza, headers);
                        ResponseEntity<String> resultMapDatosPoliza = restTemplate.postForEntity(urlPolizas, bodyGetPoliza, String.class);
                        if (resultMapDatosPoliza != null && resultMapDatosPoliza.getStatusCode().value() == 200) {
                            mapDatosPoliza = new ObjectMapper().readValue(resultMapDatosPoliza.getBody(), Map.class);
                            if (mapDatosPoliza.get("codigoMensaje").equals("CODSF1000") && mapDatosPoliza.get("Products") != null) {


                                PolizaDto objPoliza = null;
                                Map<String, Object> objMapProductos = oMapper.convertValue(mapDatosPoliza.get("Products"), Map.class);
                                List<Map<String, Object>> lstMapPolizas = oMapper.convertValue(objMapProductos.get("objetos"), ArrayList.class);
                                for (Map<String, Object> objMapPoliza : lstMapPolizas) {
                                    objPoliza = new PolizaDto();
                                    objPoliza = this.initObjPOliza(objPoliza);
                                    objPoliza.setPolizaId(objMapPoliza.get("PolicyId") != null ? objMapPoliza.get("PolicyId").toString() : "-");
                                    objPoliza.setNombreProducto(objMapPoliza.get("productName") != null ? objMapPoliza.get("productName").toString() : " - ");

                                    //objPoliza.setNumeroProducto("no-identificado");
                                    objPoliza.setNumeroProducto(objMapPoliza.get("PolicyNumber") != null ? objMapPoliza.get("PolicyNumber").toString() : "-");

                                    //objPoliza.setTipoProducto("no-identificado");
                                    objPoliza.setTipoProducto(objMapPoliza.get("PolicyType") != null ? objMapPoliza.get("PolicyType").toString() : "-");


                                    //objPoliza.setNombreAsegurado(objMapPoliza.get("NameInsured") != null ? objMapPoliza.get("NameInsured").toString() : " - ");

                                    objPoliza.setNombrePoliza(objMapPoliza.get("PolicyName") != null ? objMapPoliza.get("PolicyName").toString() : " - ");
                                    objPoliza.setFechaInicio(objMapPoliza.get("EffectiveDate") != null ? FuncionesFechas.formatearFecha_ddmmyyyy(objMapPoliza.get("EffectiveDate").toString()) : " - ");
                                    objPoliza.setFechaFin(objMapPoliza.get("ExpirationDate") != null ? FuncionesFechas.formatearFecha_ddmmyyyy(objMapPoliza.get("ExpirationDate").toString()) : " - ");


                                    objPoliza.setFrecuencia(objMapPoliza.get("PremiumFrecuency") != null ? objMapPoliza.get("PremiumFrecuency").toString() : " - ");
                                    objPoliza.setPrecio(objMapPoliza.get("Price") != null ? objMapPoliza.get("Price").toString() : " - ");


                                    // Consulta Detalle de Póliza y Pagos Pendientes por "PolicyId"
                                    // https://ap1.salesforce.com/services/apexrest/vlocity_ins/v1/integrationprocedure/bg_ti_g_getPolicyInformation
                                    String urlDetallePolizas = urlSalesforce + "/services/apexrest/vlocity_ins/v1/integrationprocedure/bg_ti_g_getPolicyInformation?PolicyId=" + objPoliza.getPolizaId() + "&Status=Pending";
                                    HttpEntity requestDetPoliza = new HttpEntity(headers);
                                    ResponseEntity<String> resultMapDatosPolizaDetalle = restTemplate.exchange(
                                            urlDetallePolizas,
                                            HttpMethod.GET,
                                            requestDetPoliza,
                                            String.class,
                                            1
                                    );
                                    if (resultMapDatosPolizaDetalle != null && resultMapDatosPolizaDetalle.getStatusCode().value() == 200) {
                                        mapDatosPolizaDetalle = new ObjectMapper().readValue(resultMapDatosPolizaDetalle.getBody(), Map.class);
                                        if (mapDatosPolizaDetalle.get("codigoMensaje").equals("CODSF1000") && mapDatosPolizaDetalle.get("Poliza") != null) {

                                            List<Map<String, Object>> lstMapPolizaDetalle = oMapper.convertValue(mapDatosPolizaDetalle.get("Poliza"), ArrayList.class);
                                            Map<String, Object> objMapPolizaDetalle = oMapper.convertValue(lstMapPolizaDetalle.get(0), Map.class);
                                            objPoliza.setNombreProducto(objMapPolizaDetalle.get("productName") != null ? objMapPolizaDetalle.get("productName").toString() : " - ");  // temproal ya q en github esta en el servicio anterior
                                            objPoliza.setNombreTomador(objMapPolizaDetalle.get("NombreTomador") != null ? objMapPolizaDetalle.get("NombreTomador").toString() : "-");
                                            objPoliza.setNombreAsegurado(objMapPolizaDetalle.get("NombreAsegurado") != null ? objMapPolizaDetalle.get("NombreAsegurado").toString() : "-");
                                            List<String> lstBeneficiarios = new ArrayList();
                                            if (objMapPolizaDetalle.get("Beneficiarios") != null) {
                                                List<Map<String, Object>> lstMapBeneficiarios = oMapper.convertValue(objMapPolizaDetalle.get("Beneficiarios"), ArrayList.class);
                                                for (Map<String, Object> objMapBeneficiarios : lstMapBeneficiarios) {
                                                    lstBeneficiarios.add(
                                                            (objMapBeneficiarios.get("Nombre") != null ? objMapBeneficiarios.get("Nombre").toString() : " - ") +
                                                                    (objMapBeneficiarios.get("Porcentaje") != null ? " (" + objMapBeneficiarios.get("Porcentaje").toString() + "%)" : " - ")
                                                    );
                                                }
                                            }
                                            objPoliza.setLstBeneficiarios(lstBeneficiarios);
                                            objPoliza.setNumeroOperacion(objMapPolizaDetalle.get("NroOperacion") != null ? objMapPolizaDetalle.get("NroOperacion").toString() : "-");
                                            objPoliza.setEstado(objMapPolizaDetalle.get("Status") != null ? objMapPolizaDetalle.get("Status").toString() : "-");
                                            objPoliza.setMontoPrima(objMapPolizaDetalle.get("PremiumAmount") != null ? objMapPolizaDetalle.get("PremiumAmount").toString() : "-");
                                            objPoliza.setTieneDocumentoPoliza((objMapPolizaDetalle.get("ArchivoBase64") != null && !objMapPolizaDetalle.get("ArchivoBase64").toString().trim().equals("")) ? true : false);


                                        } else {
                                            res.setCodigo(ConstDiccionarioMensaje.CODMW2002);
                                            res.setMensaje(ConstDiccionarioMensaje.CODMW2002_MENSAJE);
                                            return res;
                                        }
                                    }
                                    lstPolizas.add(objPoliza);
                                }

                            } else {
                                res.setCodigo(ConstDiccionarioMensaje.CODMW2002);
                                res.setMensaje(ConstDiccionarioMensaje.CODMW2002_MENSAJE);
                                return res;
                            }

                        }
                    }

                } else {
                    res.setCodigo(ConstDiccionarioMensaje.CODMW2002);
                    res.setMensaje(ConstDiccionarioMensaje.CODMW2002_MENSAJE);
                    return res;
                }
            }

            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(lstPolizas);
            return res;

        } catch (Exception ex) {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            return res;

            // aqui es recomendable registrar LOG

        }
    }


    public ResponseDto enviarSolicitudSeguro(SolicitudPolizaDto pSolicitudPolizaDto) {
        ResponseDto res = new ResponseDto();
        try {
            if (pSolicitudPolizaDto.getTipoMedioSolicitudSeguroId() == null || pSolicitudPolizaDto.getTipoMedioSolicitudSeguroId() <= 0) {
                res.setCodigo(ConstDiccionarioMensaje.CODMW2006);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2006_MENSAJE);
                return res;
            }
            if (
                    (pSolicitudPolizaDto.getNombres() == null || pSolicitudPolizaDto.getNombres().trim().equals("")) ||
                            (pSolicitudPolizaDto.getApellidos() == null || pSolicitudPolizaDto.getApellidos().trim().equals("")) ||
                            (pSolicitudPolizaDto.getTelefonoCelular() == null || pSolicitudPolizaDto.getTelefonoCelular().trim().equals("")) ||
                            (pSolicitudPolizaDto.getCiudad() == null || pSolicitudPolizaDto.getCiudad().trim().equals(""))
            ) {

                res.setCodigo(ConstDiccionarioMensaje.CODMW2004);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2004_MENSAJE);
                return res;

            }

            String medio = "";
            if (pSolicitudPolizaDto.getTipoMedioSolicitudSeguroId().equals(ConstTipoMedioSolicitudSeguro.MOVIL))
                medio = "Móvil";
            if (pSolicitudPolizaDto.getTipoMedioSolicitudSeguroId().equals(ConstTipoMedioSolicitudSeguro.WEB))
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
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudPolizaDto.getNombres() != null ? pSolicitudPolizaDto.getNombres() : " - " + "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Apellido(s)</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudPolizaDto.getApellidos() != null ? pSolicitudPolizaDto.getApellidos() : " - " + "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Teléfono o Celular</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudPolizaDto.getTelefonoCelular() != null ? pSolicitudPolizaDto.getTelefonoCelular() : "" + "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Correo</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudPolizaDto.getCorreo() != null ? pSolicitudPolizaDto.getCorreo() : "" + "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Ciudad</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudPolizaDto.getCiudad() != null ? pSolicitudPolizaDto.getCiudad() : ""+  "</td>");
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Tiene algun seguro contratado con nosotros?</strong></td>");
            if (pSolicitudPolizaDto.getTieneSeguroConNosotros()) {
                vBody.append("<td style='width: 50%; height: 18px;'>SI</td>");
            } else {
                vBody.append("<td style='width: 50%; height: 18px;'>NO</td>");
            }
            vBody.append("</tr>");
            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Tiene algun seguro contratado con otra compañia?</strong></td>");
            if (pSolicitudPolizaDto.getTieneSeguroConOtros()) {
                vBody.append("<td style='width: 50%; height: 18px;'>SI</td>");
            } else {
                vBody.append("<td style='width: 50%; height: 18px;'>NO</td>");
            }
            vBody.append("</tr>");

            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Descripción de la solicitud</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudPolizaDto.getDescripcion () != null ?  pSolicitudPolizaDto.getDescripcion ()  : "" + "</td>");
            vBody.append("</tr>");

            vBody.append("<tr style='height: 18px;'>");
            vBody.append("<td style='width: 50%; height: 18px; '><strong>Tipo de seguro de interés</strong></td>");
            vBody.append("<td style='width: 50%; height: 18px;'>" + pSolicitudPolizaDto.getTipoProductoId () != null ? iDominioDao.getDominioByDominioId(  pSolicitudPolizaDto.getTipoProductoId()).get().getDescripcion()  : "" + "</td>");
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


            emailService.enviarCorreoHtml(vDestino, vAsunto, vBody.toString());

            try {
                SolicitudSeguroEntity objInsert = new SolicitudSeguroEntity();
                objInsert.setNombres(pSolicitudPolizaDto.getNombres());
                objInsert.setApellidos(pSolicitudPolizaDto.getApellidos());
                objInsert.setTelefono_celular(pSolicitudPolizaDto.getTelefonoCelular());
                objInsert.setCiudad(pSolicitudPolizaDto.getCiudad());
                objInsert.setTipoProductoId(pSolicitudPolizaDto.getTipoProductoId());
                objInsert.setCorreo(pSolicitudPolizaDto.getCorreo());
                objInsert.setTieneSeguroNosotros(pSolicitudPolizaDto.getTieneSeguroConNosotros());
                objInsert.setTieneSeguroOtros(pSolicitudPolizaDto.getTieneSeguroConOtros());
                objInsert.setCreadoCrm(false);
                objInsert.setDescripcion(pSolicitudPolizaDto.getDescripcion());
                objInsert.setTipoMedioSolicitudSeguroId(pSolicitudPolizaDto.getTipoMedioSolicitudSeguroId());
                objInsert.setFechaRegistro(new Date());
                objInsert.setEstadoId(1000);
                iSolicitudSeguroDao.save(objInsert);

            } catch (Exception ex) {

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

    public ResponseDto descargarPoliza(String pPolicyId) {
        ResponseDto res = new ResponseDto();
        Map<String, Object> mapDatosPolizaDetalle = null;

        try {
            if (pPolicyId == null || pPolicyId.trim().equals("")) {
                res.setCodigo(ConstDiccionarioMensaje.CODMW2001);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2001_MENSAJE);
                return res;
            }

            String token = this.obtenerToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            // Consulta Detalle de Póliza y Pagos Pendientes por "PolicyId"
            // https://ap1.salesforce.com/services/apexrest/vlocity_ins/v1/integrationprocedure/bg_ti_g_getPolicyInformation
            String urlDetallePolizas = urlSalesforce + "/services/apexrest/vlocity_ins/v1/integrationprocedure/bg_ti_g_getPolicyInformation?PolicyId=" + pPolicyId + "&Status=Pending";
            HttpEntity requestDetPoliza = new HttpEntity(headers);
            ResponseEntity<String> resultMapDatosPolizaDetalle = restTemplate.exchange(
                    urlDetallePolizas,
                    HttpMethod.GET,
                    requestDetPoliza,
                    String.class,
                    1
            );
            if (resultMapDatosPolizaDetalle != null && resultMapDatosPolizaDetalle.getStatusCode().value() == 200) {
                mapDatosPolizaDetalle = new ObjectMapper().readValue(resultMapDatosPolizaDetalle.getBody(), Map.class);
                if (mapDatosPolizaDetalle.get("codigoMensaje").equals("CODSF1000") && mapDatosPolizaDetalle.get("Poliza") != null) {
                    List<Map<String, Object>> lstMapPolizaDetalle = oMapper.convertValue(mapDatosPolizaDetalle.get("Poliza"), ArrayList.class);
                    Map<String, Object> objMapPolizaDetalle = oMapper.convertValue(lstMapPolizaDetalle.get(0), Map.class);
                    if (objMapPolizaDetalle.get("ArchivoBase64") != null && !objMapPolizaDetalle.get("ArchivoBase64").toString().trim().equals("")) {
                        res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
                        res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
                        res.setElementoGenerico(objMapPolizaDetalle.get("ArchivoBase64"));
                        return res;
                    } else {
                        res.setCodigo(ConstDiccionarioMensaje.CODMW2005);
                        res.setMensaje(ConstDiccionarioMensaje.CODMW2005_MENSAJE);
                        return res;
                    }
                } else {
                    res.setCodigo(ConstDiccionarioMensaje.CODMW2005);
                    res.setMensaje(ConstDiccionarioMensaje.CODMW2005_MENSAJE);
                    return res;
                }
            } else {
                res.setCodigo(ConstDiccionarioMensaje.CODMW2005);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2005_MENSAJE);
                return res;
            }
        } catch (Exception ex) {
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            return res;
        }
    }

    private PolizaDto initObjPOliza(PolizaDto pPolizasDto) {
        pPolizasDto.setPolizaId(" - ");
        pPolizasDto.setNombreProducto(" - ");
        pPolizasDto.setNumeroProducto(" - ");
        pPolizasDto.setNombreAsegurado(" - ");
        pPolizasDto.setNombreTomador(" - ");
        pPolizasDto.setLstBeneficiarios(new ArrayList<>());
        pPolizasDto.setNumeroOperacion(" - ");
        pPolizasDto.setNombrePoliza(" - ");
        pPolizasDto.setFechaInicio(" - ");
        pPolizasDto.setFechaFin(" - ");
        pPolizasDto.setEstado(" - ");
        pPolizasDto.setTipoProducto(" - ");
        pPolizasDto.setFrecuencia(" - ");
        pPolizasDto.setMontoPrima(" - ");
        pPolizasDto.setPrecio(" - ");
        return pPolizasDto;

    }
}
