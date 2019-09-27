package com.josegiron.smspam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    EditText txtUsuario, txtPassword;
    private FirebaseAuth mAuth;
    private String email = "";
    private String passw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        101);
            }
        }
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsuario.requestFocus();

        Button btnEnviarLogin = findViewById(R.id.btnLogin);
        btnEnviarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtUsuario.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()){
                    //getLogin(txtUsuario.getText().toString() , txtPassword.getText().toString());
                    email = txtUsuario.getText().toString();
                    passw = txtPassword.getText().toString();
                    getLogin(email, passw);
                }else{
                    Toast.makeText(MainActivity.this, "TODOS LOS CAMPOS SON REQUERIDOS!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //autenticacion anterior

        /*txtUsuario.requestFocus();
        InspeccionesDbHelper dbHelper = new InspeccionesDbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {
                InspeccionContract.LogUserEntry.ID,
                InspeccionContract.LogUserEntry.FECHA___V,
                InspeccionContract.LogUserEntry.USUARIO_I
        };
        Cursor cursor = db.query(InspeccionContract.LogUserEntry.TABLE_NAME, //Table to query
                columns,                    //columns to return
                null,                  //columns for the WHERE clause
                null,              //The values for the WHERE clause
                null,              //group the rows
                null,               //filter by row groups
                null);             //The sort order
        int numeroFilas = cursor.getCount();
        if(numeroFilas > 0) {
            if(cursor.moveToFirst() == true) {
                String usuarioLoguiado = cursor.getString(2);
                Intent intent = new Intent(this, SmsActivity.class);
                intent.putExtra("idUsuario", usuarioLoguiado);
                startActivity(intent);
                finish();
            }
        }
        db.close();*/

        /*Firebase*/
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(this, SmsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getLogin(String usuario_p , String pass_p){
        final ProgressDialog mDialog = ProgressDialog.show(this, "", "Un momento por favor...", true, false );

        /*FireBase*/
        mAuth.signInWithEmailAndPassword(usuario_p, pass_p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mDialog.cancel();
                    mDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, SmsActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Usuario y/o contraseña errados!.",
                            Toast.LENGTH_SHORT).show();
                    mDialog.cancel();
                    mDialog.dismiss();
                }
            }
        });

        /*Proceso de Login anterior*/
        /*InspeccionesDbHelper dbHelper = new InspeccionesDbHelper(MainActivity.this);
        if(dbHelper.checkUser(usuario_p, pass_p)){
            int idUsuario = 0;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] columns = {
                    InspeccionContract.UsuariosEntry.ID,
                    InspeccionContract.UsuariosEntry.NOMBRES_V
            };
            String selection = InspeccionContract.UsuariosEntry.IDENTIF_V + " = ?" + " AND " + InspeccionContract.UsuariosEntry.PASS____V + " = ?";
            String[] selectionArgs = {usuario_p, pass_p};
            Cursor cursor = db.query(InspeccionContract.UsuariosEntry.TABLE_NAME, //Table to query
                    columns,                    //columns to return
                    selection,                  //columns for the WHERE clause
                    selectionArgs,              //The values for the WHERE clause
                    null,              //group the rows
                    null,               //filter by row groups
                    null);             //The sort order

            if(cursor.moveToFirst() == true) {
                idUsuario       = cursor.getInt(0);
                String IdUsuario = cursor.getString(0);
                Date currentTime = Calendar.getInstance().getTime();
                dbHelper.mockLog(db, new Log(1, idUsuario, currentTime.toString(), 1));
                mDialog.cancel();
                mDialog.dismiss();
                db.close();
                Intent intent = new Intent(this, SmsActivity.class);
                intent.putExtra("idUsuario", IdUsuario);
                startActivity(intent);
                finish();
            }
        }else{
            Toast.makeText(MainActivity.this, "Usuario y/o contraseña errados!.",
                    Toast.LENGTH_SHORT).show();
            mDialog.cancel();
            mDialog.dismiss();
        }*/
    }
}
