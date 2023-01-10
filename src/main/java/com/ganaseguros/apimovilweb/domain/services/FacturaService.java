package com.ganaseguros.apimovilweb.domain.services;

import com.ganaseguros.apimovilweb.domain.dto.DatosFacturaDto;
import com.ganaseguros.apimovilweb.domain.dto.ResponseDto;
import com.ganaseguros.apimovilweb.utils.FuncionesGenericos;
import com.ganaseguros.apimovilweb.utils.constantes.ConstDiccionarioMensaje;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class FacturaService {
    public ResponseDto consultaFacturas(Map objRequest) {
        String vNroDocumento = objRequest.get("nroDocumento") != null ? objRequest.get("nroDocumento").toString() : "";
        String vCiudadExpedido = objRequest.get("ciudadExpedido") != null ? objRequest.get("ciudadExpedido").toString() : ""; // de momento esto no se utiliza, el Servicio de SF no usa
        String vFechaNac = objRequest.get("fechaNacimiento") != null ? objRequest.get("fechaNacimiento").toString() : "";
        String vComplemento = objRequest.get("complemento") != null ? objRequest.get("complemento").toString() : "";

        List<DatosFacturaDto> lstDatosFacturaDto = new ArrayList<>();
        ResponseDto res = new ResponseDto();

        try{
            if (vNroDocumento.trim().equals("") || vFechaNac.trim().equals("")) {
                res.setCodigo(ConstDiccionarioMensaje.CODMW2001);
                res.setMensaje(ConstDiccionarioMensaje.CODMW2001_MENSAJE);
                return res;
            }

            DatosFacturaDto factura1 = new DatosFacturaDto();
            factura1.setFechaFactura("02/01/2022");
            factura1.setNitEmisor("1020343027");
            factura1.setNombreComercial("GANASEGUROS S.A.");
            factura1.setNumeroFactura("12232");
            factura1.setMonto("293.00");
            DatosFacturaDto factura2 = new DatosFacturaDto();
            factura2.setFechaFactura("02/02/2022");
            factura2.setNitEmisor("1020343027");
            factura2.setNombreComercial("GANASEGUROS S.A.");
            factura2.setNumeroFactura("12233");
            factura2.setMonto("293.00");
            DatosFacturaDto factura3 = new DatosFacturaDto();
            factura3.setFechaFactura("02/03/2022");
            factura3.setNitEmisor("1020343027");
            factura3.setNombreComercial("GANASEGUROS S.A.");
            factura3.setNumeroFactura("12234");
            factura3.setMonto("293.00");

            lstDatosFacturaDto.add(factura1);
            lstDatosFacturaDto.add(factura2);
            lstDatosFacturaDto.add(factura3);
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(lstDatosFacturaDto);
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
            System.out.println(ex.toString());
        }
        return res;
    }
    public  ResponseDto descargarFactura(){
        ResponseDto res = new ResponseDto();
        try{
            String facturaBase64 = FuncionesGenericos.donwnloadUrlAndConverToBase64("https://www.gob.mx/cms/uploads/attachment/file/293173/SANCHEZ_ROMEA_LUIS_ALFREDO_DEL_22_AL_23_DE_NOVIEMBRE_COMPROBANTE_9.pdf");
            res.setCodigo(ConstDiccionarioMensaje.CODMW1000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW1000_MENSAJE);
            res.setElementoGenerico(facturaBase64);
        }catch (Exception ex){
            res.setCodigo(ConstDiccionarioMensaje.CODMW2000);
            res.setMensaje(ConstDiccionarioMensaje.CODMW2000_MENSAJE);
        }
        return res;
    }
}
