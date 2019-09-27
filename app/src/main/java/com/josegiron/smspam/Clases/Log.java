package com.josegiron.smspam.Clases;
import android.content.ContentValues;
import com.josegiron.smspam.Datos.InspeccionContract;

public class Log {
    private int log_id;
    private int log_usu_id;
    private String log_fecha;
    private int log_estado;

    public Log(int log_id, int log_usu_id, String log_fecha, int log_estado) {
        this.log_id = log_id;
        this.log_usu_id = log_usu_id;
        this.log_fecha = log_fecha;
        this.log_estado = log_estado;
    }


    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public int getLog_usu_id() {
        return log_usu_id;
    }

    public void setLog_usu_id(int log_usu_id) {
        this.log_usu_id = log_usu_id;
    }

    public String getLog_fecha() {
        return log_fecha;
    }

    public void setLog_fecha(String log_fecha) {
        this.log_fecha = log_fecha;
    }

    public int getLog_estado() {
        return log_estado;
    }

    public void setLog_estado(int log_estado) {
        this.log_estado = log_estado;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(InspeccionContract.LogUserEntry.ID, log_id);
        values.put(InspeccionContract.LogUserEntry.USUARIO_I, log_usu_id);
        values.put(InspeccionContract.LogUserEntry.ESTADO__I, log_estado);
        values.put(InspeccionContract.LogUserEntry.FECHA___V, log_fecha);
        return values;
    }
}
