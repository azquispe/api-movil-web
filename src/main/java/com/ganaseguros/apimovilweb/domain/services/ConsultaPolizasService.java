package com.ganaseguros.apimovilweb.domain.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ganaseguros.apimovilweb.domain.dao.IDominioDao;
import com.ganaseguros.apimovilweb.domain.dao.ISolicitudSeguroDao;
import com.ganaseguros.apimovilweb.domain.dto.PolizaDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.utils.FuncionesFechas;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;
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

    @Value("${url.salesforce}")
    private String urlSalesforce;
    @Value("${url.salesforce.grant_type}")
    private String urlSalesforceGrantType;
    @Value("${url.salesforce.cliente_id}")
    private String urlSalesforceClienteId;
    @Value("${url.salesforce.client_secret}")
    private String urlSalesforceClientSecret;
    @Value("${url.salesforce.username}")
    private String urlSalesforceUsername;
    @Value("${url.salesforce.password}")
    private String urlSalesforcePassword;




    ObjectMapper oMapper = new ObjectMapper();

    @Autowired
    private EmailService emailService;

    @Autowired
    private ISolicitudSeguroDao iSolicitudSeguroDao;

    @Autowired
    private IDominioDao iDominioDao;


    public String obtenerToken() throws URISyntaxException {
        ResponseEntity<Map> resultMap = restTemplate.postForEntity(new URI(urlSalesforce + "/services/oauth2/token?grant_type="+urlSalesforceGrantType+"&" +
                "client_id="+urlSalesforceClienteId+"&" +
                "client_secret=" +urlSalesforceClientSecret+
                "&username="+urlSalesforceUsername+"&password="+urlSalesforcePassword), new HashMap<>(), Map.class);
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
