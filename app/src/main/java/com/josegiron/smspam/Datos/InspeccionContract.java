package com.josegiron.smspam.Datos;

import android.provider.BaseColumns;

public class InspeccionContract {

    public static  abstract class UsuariosEntry implements BaseColumns {

        public static final String TABLE_NAME = "Msg_usuarios";

        public static final String ID = "usu_id";
        public static final String NOMBRES_V = "usu_nombres";
        public static final String PASS____V = "usu_password";
        public static final String IDENTIF_V = "usu_identificacion";
        public static final String ULTIMO__V = "usu_ultimo_login";
        public static final String ESTADO__I = "usu_estado";

    }

    public static  abstract class LogUserEntry implements BaseColumns {

        public static final String TABLE_NAME = "Msg_log";

        public static final String ID = "log_id";
        public static final String USUARIO_I = "log_usu_id";
        public static final String FECHA___V = "log_fecha";
        public static final String ESTADO__I = "log_estado";

    }

}
