package com.josegiron.smspam.Clases;

public class UrlData {
    private String uuid;
    private  String nombre;
    private String primerNumero;
    private String segundoNumero;
    private String url;

    public UrlData(String uuid, String nombre, String primerNumero, String segundoNumero, String url) {
        this.uuid = uuid;
        this.nombre = nombre;
        this.primerNumero = primerNumero;
        this.segundoNumero = segundoNumero;
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerNumero() {
        return primerNumero;
    }

    public void setPrimerNumero(String primerNumero) {
        this.primerNumero = primerNumero;
    }

    public String getSegundoNumero() {
        return segundoNumero;
    }

    public void setSegundoNumero(String segundoNumero) {
        this.segundoNumero = segundoNumero;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


