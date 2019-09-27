package com.josegiron.smspam.Datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.josegiron.smspam.Clases.*;

public class InspeccionesDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "msg.db";

    public InspeccionesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + InspeccionContract.UsuariosEntry.TABLE_NAME + " ("
                + InspeccionContract.UsuariosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + InspeccionContract.UsuariosEntry.ID + " INTEGER NOT NULL,"
                + InspeccionContract.UsuariosEntry.NOMBRES_V + " TEXT NOT NULL,"
                + InspeccionContract.UsuariosEntry.PASS____V + " TEXT NOT NULL,"
                + InspeccionContract.UsuariosEntry.IDENTIF_V + " TEXT NOT NULL,"
                + InspeccionContract.UsuariosEntry.ULTIMO__V + " TEXT NOT NULL,"
                + InspeccionContract.UsuariosEntry.ESTADO__I  + " TEXT NOT NULL,"
                + "UNIQUE (" + InspeccionContract.UsuariosEntry.ID  + "))");

        db.execSQL("CREATE TABLE " + InspeccionContract.LogUserEntry.TABLE_NAME + " ("
                + InspeccionContract.LogUserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + InspeccionContract.LogUserEntry.ID + " INTEGER NOT NULL,"
                + InspeccionContract.LogUserEntry.USUARIO_I + " TEXT NOT NULL,"
                + InspeccionContract.LogUserEntry.FECHA___V + " TEXT NOT NULL,"
                + InspeccionContract.LogUserEntry.ESTADO__I + " TEXT NOT NULL,"
                + "UNIQUE (" + InspeccionContract.LogUserEntry.ID  + "))");

        mockData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + InspeccionContract.LogUserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE " + InspeccionContract.UsuariosEntry.TABLE_NAME);
        onCreate(db);
    }

    private void mockData(SQLiteDatabase sqLiteDatabase) {
        /*Insertamos los usuarios*/
        mockUsers(sqLiteDatabase, new Usuarios(1, "Usuario 1",
                "vhi123456", "usuario1",
                "2019-04-26", "1"));
        mockUsers(sqLiteDatabase, new Usuarios(2, "Usuario 2",
                "vhi123456", "usuario2",
                "2019-04-26", "1"));
        mockUsers(sqLiteDatabase, new Usuarios(3, "Usuario 3",
                "vhi123456", "usuario3",
                "2019-04-26", "1"));
        mockUsers(sqLiteDatabase, new Usuarios(4, "Usuario 4",
                "vhi123456", "usuario4",
                "2019-04-26", "1"));
        mockUsers(sqLiteDatabase, new Usuarios(5, "Usuario 5",
                "vhi123456", "usuario5",
                "2019-04-26", "1"));
        mockUsers(sqLiteDatabase, new Usuarios(6, "Usuario 6",
                "vhi123456", "usuario6",
                "2019-04-26", "1"));
        mockUsers(sqLiteDatabase, new Usuarios(7, "administrador",
                "adminsms123", "administrador",
                "2019-08-05", "1"));
    }


    public long mockUsers(SQLiteDatabase db, Usuarios user) {
        return db.insert(
                InspeccionContract.UsuariosEntry.TABLE_NAME,
                null,
                user.toContentValues());
    }


    public long mockLog(SQLiteDatabase db, Log log) {
        return db.insert(
                InspeccionContract.LogUserEntry.TABLE_NAME,
                null,
                log.toContentValues());
    }

    public boolean checkUser(String usuario, String password) {

        String[] columns = {
                InspeccionContract.UsuariosEntry._ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = InspeccionContract.UsuariosEntry.IDENTIF_V + " = ?" + " AND " + InspeccionContract.UsuariosEntry.PASS____V + " = ?";
        String[] selectionArgs = {usuario, password};
        Cursor cursor = db.query(InspeccionContract.UsuariosEntry.TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,              //group the rows
                null,               //filter by row groups
                null);             //The sort order

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

}
