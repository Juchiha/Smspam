package com.josegiron.smspam.Clases;
import android.content.ContentValues;
import com.josegiron.smspam.Datos.InspeccionContract;

public class Usuarios {

    private int usu_id;
    private String usu_nombres;
    private String usu_password;
    private String usu_identificacion;
    private String usu_ultimo_login;
    private String usu_estado;

    public Usuarios(int usu_id, String usu_nombres, String usu_password, String usu_identificacion, String usu_ultimo_login, String usu_estado) {
        this.usu_id = usu_id;
        this.usu_nombres = usu_nombres;
        this.usu_password = usu_password;
        this.usu_identificacion = usu_identificacion;
        this.usu_ultimo_login = usu_ultimo_login;
        this.usu_estado = usu_estado;
    }

    public int getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(int usu_id) {
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

    public String getUsu_identificacion() {
        return usu_identificacion;
    }

    public void setUsu_identificacion(String usu_identificacion) {
        this.usu_identificacion = usu_identificacion;
    }

    public String getUsu_ultimo_login() {
        return usu_ultimo_login;
    }

    public void setUsu_ultimo_login(String usu_ultimo_login) {
        this.usu_ultimo_login = usu_ultimo_login;
    }

    public String getUsu_estado() {
        return usu_estado;
    }

    public void setUsu_estado(String usu_estado) {
        this.usu_estado = usu_estado;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(InspeccionContract.UsuariosEntry.ID, usu_id);
        values.put(InspeccionContract.UsuariosEntry.NOMBRES_V, usu_nombres);
        values.put(InspeccionContract.UsuariosEntry.IDENTIF_V, usu_identificacion);
        values.put(InspeccionContract.UsuariosEntry.ULTIMO__V, usu_ultimo_login);
        values.put(InspeccionContract.UsuariosEntry.PASS____V, usu_password);
        values.put(InspeccionContract.UsuariosEntry.ESTADO__I, usu_estado);
        return values;
    }

}
