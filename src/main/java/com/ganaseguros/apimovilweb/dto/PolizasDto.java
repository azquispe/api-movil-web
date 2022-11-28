package com.ganaseguros.apimovilweb.dto;

import java.util.List;

public class PolizasDto {
       private String polizaId;
       private String nombreProducto;
       private String numeroProducto;
       private String nombreAsegurado;
       private String nombreTomador;
       private List<String> lstBeneficiarios;
       private String numeroOperacion;
       private String nombrePoliza;
       private String fechaInicio;
       private String fechaFin;
       private String estado;
       private String tipoProducto;
       private String frecuencia;
       private String montoPrima;
       private String precio;

       private Boolean tieneDocumentoPoliza;

       public Boolean getTieneDocumentoPoliza() {
              return tieneDocumentoPoliza;
       }

       public void setTieneDocumentoPoliza(Boolean tieneDocumentoPoliza) {
              this.tieneDocumentoPoliza = tieneDocumentoPoliza;
       }

       public String getPolizaId() {
              return polizaId;
       }

       public void setPolizaId(String polizaId) {
              this.polizaId = polizaId;
       }

       public String getNombreProducto() {
              return nombreProducto;
       }

       public void setNombreProducto(String nombreProducto) {
              this.nombreProducto = nombreProducto;
       }

       public String getNumeroProducto() {
              return numeroProducto;
       }

       public void setNumeroProducto(String numeroProducto) {
              this.numeroProducto = numeroProducto;
       }

       public String getNombreAsegurado() {
              return nombreAsegurado;
       }

       public void setNombreAsegurado(String nombreAsegurado) {
              this.nombreAsegurado = nombreAsegurado;
       }

       public String getNombreTomador() {
              return nombreTomador;
       }

       public void setNombreTomador(String nombreTomador) {
              this.nombreTomador = nombreTomador;
       }

       public List<String> getLstBeneficiarios() {
              return lstBeneficiarios;
       }

       public void setLstBeneficiarios(List<String> lstBeneficiarios) {
              this.lstBeneficiarios = lstBeneficiarios;
       }

       public String getNumeroOperacion() {
              return numeroOperacion;
       }

       public void setNumeroOperacion(String numeroOperacion) {
              this.numeroOperacion = numeroOperacion;
       }

       public String getNombrePoliza() {
              return nombrePoliza;
       }

       public void setNombrePoliza(String nombrePoliza) {
              this.nombrePoliza = nombrePoliza;
       }

       public String getFechaInicio() {
              return fechaInicio;
       }

       public void setFechaInicio(String fechaInicio) {
              this.fechaInicio = fechaInicio;
       }

       public String getFechaFin() {
              return fechaFin;
       }

       public void setFechaFin(String fechaFin) {
              this.fechaFin = fechaFin;
       }

       public String getEstado() {
              return estado;
       }

       public void setEstado(String estado) {
              this.estado = estado;
       }

       public String getTipoProducto() {
              return tipoProducto;
       }

       public void setTipoProducto(String tipoProducto) {
              this.tipoProducto = tipoProducto;
       }

       public String getFrecuencia() {
              return frecuencia;
       }

       public void setFrecuencia(String frecuencia) {
              this.frecuencia = frecuencia;
       }

       public String getMontoPrima() {
              return montoPrima;
       }

       public void setMontoPrima(String montoPrima) {
              this.montoPrima = montoPrima;
       }

       public String getPrecio() {
              return precio;
       }

       public void setPrecio(String precio) {
              this.precio = precio;
       }
}
