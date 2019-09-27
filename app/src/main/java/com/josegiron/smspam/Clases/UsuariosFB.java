package com.josegiron.smspam.Clases;

public class UsuariosFB {
    private String usu_id;
    private String usu_nombres;
    private String usu_password;
    private String usu_mail;
    private String usu_rol;

    public UsuariosFB(String usu_id, String usu_nombres, String usu_password, String usu_mail, String usu_rol) {
        this.usu_id = usu_id;
        this.usu_nombres = usu_nombres;
        this.usu_password = usu_password;
        this.usu_mail = usu_mail;
        this.usu_rol = usu_rol;
    }

    public String getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(String usu_id) {
        this.usu_id = usu_id;
    }

    public String getUsu_nombres() {
        return usu_nombres;
    }

    public void setUsu_nombres(String usu_nombres) {
        this.usu_nombres = usu_nombres;
    }

    public String getUsu_password() {
        return usu_password;
    }

    public void setUsu_password(String usu_password) {
        this.usu_password = usu_password;
    }

    public String getUsu_mail() {
        return usu_mail;
    }

    public void setUsu_mail(String usu_mail) {
        this.usu_mail = usu_mail;
    }

    public String getUsu_rol() {
        return usu_rol;
    }

    public void setUsu_rol(String usu_rol) {
        this.usu_rol = usu_rol;
    }
}
