package com.ganaseguros.apimovilweb.dto;

public class SolicitudPolizaDto {
    private String nombres;
    private String apellidos;
    private String telefonoCelular;
    private String correo;
    private String ciudad;
    private Boolean tieneSeguroConNosotros;
    private Boolean tieneSeguroConOtros;
    private String tipoSeguroInteresado;

    private Integer tipoMedioSolicitudSeguro;

    public Integer getTipoMedioSolicitudSeguro() {
        return tipoMedioSolicitudSeguro;
    }

    public void setTipoMedioSolicitudSeguro(Integer tipoMedioSolicitudSeguro) {
        this.tipoMedioSolicitudSeguro = tipoMedioSolicitudSeguro;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Boolean getTieneSeguroConNosotros() {
        return tieneSeguroConNosotros;
    }

    public void setTieneSeguroConNosotros(Boolean tieneSeguroConNosotros) {
        this.tieneSeguroConNosotros = tieneSeguroConNosotros;
    }

    public Boolean getTieneSeguroConOtros() {
        return tieneSeguroConOtros;
    }

    public void setTieneSeguroConOtros(Boolean tieneSeguroConOtros) {
        this.tieneSeguroConOtros = tieneSeguroConOtros;
    }

    public String getTipoSeguroInteresado() {
        return tipoSeguroInteresado;
    }

    public void setTipoSeguroInteresado(String tipoSeguroInteresado) {
        this.tipoSeguroInteresado = tipoSeguroInteresado;
    }
}
