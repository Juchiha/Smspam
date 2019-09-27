package com.josegiron.smspam;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.josegiron.smspam.Clases.AdaptadorFirebase;
import com.josegiron.smspam.Clases.UrlData;
import com.josegiron.smspam.Clases.UsuariosFB;
import com.josegiron.smspam.Datos.InspeccionContract;
import com.josegiron.smspam.Datos.InspeccionesDbHelper;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SmsActivity extends AppCompatActivity {
    TextView txtMessageEnviar;
    EditText txtMensajeEnviarEdt, txtUrlCargarTexto;
    Spinner txtUrlCargar;
    private String idUsuario, password, tipo, email_;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private ProgressBar progressBar;
    private TextView txtAVance, txtNumeroEnviado;
    Button btnSalir, btnEnvioMsg, btnChangePassword, btnUsuarios;
    ProgressBar bar;
    PackageManager pm;
    Handler handler = new Handler(Looper.getMainLooper());
    String urlACargar = "NULL";
    String[] uidUrl = null;
    String idUrlNombre = null;
    UrlData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);



        mAuth = FirebaseAuth.getInstance();
        validateUser();


        txtMessageEnviar = findViewById(R.id.txtMessageEnviar);
        txtMensajeEnviarEdt = findViewById(R.id.txtMensajeEnviarEdit);
        txtUrlCargar = findViewById(R.id.SpinerUrl);
        txtUrlCargarTexto = findViewById(R.id.txtUrlCargarText);

        progressBar = findViewById(R.id.prgressNotification);
        txtAVance = findViewById(R.id.txtAvance);
        txtNumeroEnviado = findViewById(R.id.txtNumerosTrabajados);

        /*Bundle parametros = this.getIntent().getExtras();
        if(parametros != null) {
            //idUsuario = parametros.getString("idUsuario");
            //validateUser(idUsuario);
        }*/

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        101);
            }
        }

        btnEnvioMsg = findViewById(R.id.btnEnviarMsg);
        pm = this.getPackageManager();
        btnEnvioMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtMensajeEnviarEdt.getText().toString().equals("") && txtMensajeEnviarEdt.getText().toString().length() > 4 && (!urlACargar.equals("NULL") || !txtUrlCargarTexto.getText().toString().equals("") )){
                    //new getDatos().execute(txtMensajeEnviarEdt.getText().toString(), "Pruebas", "SendMSG");
                    AlertDialog.Builder builder = new AlertDialog.Builder( SmsActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View vista = inflater.inflate(R.layout.dialog_signin, null);
                    final EditText passwordEditText = vista.findViewById(R.id.password);
                    builder.setView(vista)
                            .setPositiveButton("Enviar Msgs", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(password.equals(passwordEditText.getText().toString())){
                                        /*Procede el envio de los mensajes*/

                                        //conectAndSendMsg();
                                        if(urlACargar != "NULL"){
                                            new getDatos().execute(urlACargar, "Pruebas", "SendMSG");
                                        }else{
                                            if(!txtUrlCargarTexto.getText().toString().equals("")){
                                                new getDatos().execute(txtUrlCargarTexto.getText().toString(), "Pruebas", "SendMSG");
                                            }else{

                                            }

                                        }

                                    }else{
                                        Toast.makeText(SmsActivity.this, "Password Errado!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SmsActivity.this);
                    builder.setMessage("Ingresa un mensaje valido para enviar y/o selecciona la Url del archivo!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    txtMensajeEnviarEdt.requestFocus();
                                }
                            });
                    builder.show();
                }


            }
        });

        btnChangePassword = findViewById(R.id.btnChangePass);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder( SmsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View vista = inflater.inflate(R.layout.dialog_pasword_change, null);
                final EditText passwordText = vista.findViewById(R.id.passwordChange);
                final EditText passwordChanText = vista.findViewById(R.id.passwordChangeRepetir);
                builder.setView(vista)
                        .setPositiveButton("Cambiar contraseña", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!passwordText.getText().toString().equals("") && !passwordChanText.getText().toString().equals("") ){
                                    if(passwordText.getText().toString().length() > 4 && passwordChanText.getText().toString().length() > 4){
                                        if(passwordText.getText().toString().equals(passwordChanText.getText().toString())){
                                            /*Procede el cambio de password*/

                                            final FirebaseUser user = mAuth.getCurrentUser();
                                            AuthCredential credential = EmailAuthProvider
                                                    .getCredential(email_, password);
                                            user.reauthenticate(credential)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                user.updatePassword(passwordText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(!task.isSuccessful()){
                                                                            Toast.makeText(SmsActivity.this, "La contraseña no ha sido mofificada!", Toast.LENGTH_LONG).show();
                                                                        }else {
                                                                            Toast.makeText(SmsActivity.this, "La contraseña ha sido mofificada!", Toast.LENGTH_LONG).show();
                                                                            dbRef = FirebaseDatabase.getInstance().getReference();
                                                                            String id = mAuth.getCurrentUser().getUid();
                                                                            DatabaseReference usersRef = dbRef.child("Users");

                                                                            Map<String, Object> userUpdates = new HashMap<>();
                                                                            userUpdates.put(id+"/correo", email_);
                                                                            userUpdates.put(id+"/rol", tipo);
                                                                            userUpdates.put(id+"/password", passwordText.getText().toString());
                                                                            userUpdates.put(id+"/nombre", idUsuario);

                                                                            usersRef.updateChildren(userUpdates);
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                Log.d("SMS APP", "Error auth failed");
                                                            }
                                                        }
                                            });
                                        }else{
                                            Toast.makeText(SmsActivity.this, "Las contraseñas no coinciden!", Toast.LENGTH_LONG).show();
                                        }

                                    }else{
                                        Toast.makeText(SmsActivity.this, "Los contraseñas deben tener minimo 4 caracteres!", Toast.LENGTH_LONG).show();
                                    }

                                }else{
                                    Toast.makeText(SmsActivity.this, "Los valores estan vacios!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });

        btnUsuarios = findViewById(R.id.btnUsuarios);
        btnUsuarios.setEnabled(false);
        btnUsuarios.setVisibility(View.INVISIBLE);
        btnUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inet = new Intent(SmsActivity.this, UsuariosActivity.class);
                startActivity(inet);
            }
        });

        btnSalir = findViewById(R.id.btnSalirApp);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*InspeccionesDbHelper dbHelper = new InspeccionesDbHelper(SmsActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int respuesta = db.delete(InspeccionContract.LogUserEntry.TABLE_NAME, null, null);
                Intent intent = new Intent(SmsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();*/
                /*Firebase*/
                mAuth.signOut();
                Intent intent = new Intent(SmsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
        });

        llenarArrayList();

    }

    /*Progrese Bar*/
    private void mostrarProgressBar(final BufferedReader in, final BufferedReader newB){

        if(in == null){
            showToast("INGRESA O SELECCIONA URL VALIDA!", "toast");
        }else{
            progressBar.setVisibility(View.VISIBLE);
            txtAVance.setVisibility(View.VISIBLE);
            txtNumeroEnviado.setVisibility(View.VISIBLE);
            txtMensajeEnviarEdt.setEnabled(false);
            txtUrlCargar.setEnabled(false);
            btnSalir.setEnabled(false);
            btnChangePassword.setEnabled(false);
            btnEnvioMsg.setEnabled(false);
            txtUrlCargarTexto.setEnabled(false);
            final String[] numeros = new String[20000];
            Thread thread =new Thread(){
                @Override
                public void run() {
                    super.run();
                    try{

                        String str1 = null;
                        int finalCoundown = 0;

                        while ((str1 = in.readLine()) != null) {
                            numeros[finalCoundown] = str1;
                            finalCoundown++;
                        }


                        Log.d("finalCoundown ::> "," _:::> "+ finalCoundown);
                        String str2 = null;
                        int i = 0;
                        String mensage = txtMensajeEnviarEdt.getText().toString();
                        SmsManager sms = SmsManager.getDefault();

                        ArrayList messagePart =  sms.divideMessage(mensage);
                        String primerN = "";
                        String segundN = "";


                        if(!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
                                !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)){
                            showToast("Lo sentimos, tu dispositivo probablemente no pueda enviar SMS...", "toast");
                        }else{

                            for(i = 0; i < finalCoundown; i++){
                                /*if(i == 20){
                                    break;
                                }*/

                                //finalCoundown == 100%
                                //i == ?
                                if(i == 0){
                                    primerN = numeros[i];
                                }

                                Thread.sleep(2000);
                                int resultado =  (i * 100) / finalCoundown   ;
                                Log.d("Resultado ::> "," _:::> "+ resultado);
                                progressBar.setProgress(resultado);
                                txtAVance.setText(resultado + "/100%");
                                txtNumeroEnviado.setText("Enviando mensage a " +  numeros[i]);
                                sms.sendMultipartTextMessage( numeros[i], null, messagePart, null, null);
                                segundN = numeros[i];
                            }
                        }
                        Log.d("Este es I ::> ", "I ::> "+ i);

                        /*Date currentTime = Calendar.getInstance().getTime();


                        if(!urlACargar.equals("NULL")){
                            Map<String, String> map = new HashMap<>();
                            map.put("ultimoEnvio", currentTime.toString());
                            map.put("nombre", data.getNombre());
                            map.put("url", data.getUrl());
                            map.put("primerNumero", data.getPrimerNumero());
                            map.put("segundoNumero", data.getSegundoNumero());
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Url").child(idUrlNombre).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){
                                        Log.d("Proceso Terminado" , "::> Exito");
                                        urlACargar = "NULL";
                                    }
                                }
                            });
                        }else{
                            Map<String, String> map = new HashMap<>();
                            map.put("ultimoEnvio", currentTime.toString());
                            map.put("nombre", txtUrlCargarTexto.getText().toString());
                            map.put("url", txtUrlCargarTexto.getText().toString());
                            map.put("primerNumero", primerN);
                            map.put("segundoNumero", segundN);
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Url").child("New_"+primerN+"_to_"+segundN).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()){
                                        Log.d("Proceso Terminado" , "::> Exito");
                                    }
                                }
                            });
                        }*/

                        progressBar.setVisibility(View.INVISIBLE);
                        txtAVance.setVisibility(View.INVISIBLE);
                        txtNumeroEnviado.setVisibility(View.INVISIBLE);

                        showToast("Proceso Terminado Se Enviaron "+i+" MSG", "alert");

                    }catch (IOException e) {
                        e.printStackTrace();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }

    }


    /*Obtenemos los numeros del TXT*/
    private void conectAndSendMsg() {
        final ProgressDialog mDialog = ProgressDialog.show(this, "", "Un momento por favor enviando mensajes...", true, false );
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    //"http://lapolla.com.co/images/numeros.txt"
                    //URL url = new URL(txtUrlCargar.getText().toString());
                    URL url = new URL("http://lapolla.com.co/images/numeros.txt");
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                    String str1 = null;
                    String mensage = txtMensajeEnviarEdt.getText().toString();
                    SmsManager sms = SmsManager.getDefault();
                    ArrayList messagePart =  sms.divideMessage(mensage);
                    if(!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
                            !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)){
                        Toast.makeText(SmsActivity.this, "Lo sentimos, tu dispositivo probablemente no pueda enviar SMS...", Toast.LENGTH_SHORT).show();
                    }else{
                       //int i = 0;

                        mostrarProgressBar(in, in);
                        /*while ((str1 = in.readLine()) != null) {
                            i++;
                            /if(finalCoundown > 0){
                                //finalCoundown == 100%
                                //i == ?
                                float resultado = i * 100 / finalCoundown;
                                txtAVance.setText(resultado+"/100%");
                                txtNumeroEnviado.setText("Enviando mensage a "+str1);
                            }
                            /*int re = i % 10;
                            if(re != 0){
                                Thread.sleep(500);
                            }else{
                                Thread.sleep(20000);
                            }
                            sms.sendMultipartTextMessage(str1, null, messagePart, null, null);
                            Log.d("Esto enviado ::> ", "I = "+i);*

                        }*/

                    }

                    in.close();
                    mDialog.cancel();
                    mDialog.dismiss();

                }catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } /*catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

            }
        };
        thread.start();
    }


    public void showToast(final String message, final String type) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if(type.equals("alert")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SmsActivity.this);
                        builder.setMessage(message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //"No hago nada"
                                    }
                                });
                        builder.show();

                        txtMensajeEnviarEdt.setEnabled(true);
                        txtUrlCargar.setEnabled(true);
                        btnSalir.setEnabled(true);
                        btnChangePassword.setEnabled(true);
                        btnEnvioMsg.setEnabled(true);
                        txtUrlCargarTexto.setText("");
                        txtUrlCargarTexto.setEnabled(true);
                        txtAVance.setText( "0/100%");
                        txtNumeroEnviado.setText("Preparado... ");

                    }else {
                        Toast.makeText(SmsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*Obtenemos el nombre del usuario logueado*/
    private void validateUser(){

        /*Firebase*/
        dbRef = FirebaseDatabase.getInstance().getReference();
        String id = mAuth.getCurrentUser().getUid();
        dbRef.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String nombr_ = dataSnapshot.child("nombre").getValue().toString();
                    email_ = dataSnapshot.child("correo").getValue().toString();
                    String passw_ = dataSnapshot.child("password").getValue().toString();
                    tipo = dataSnapshot.child("rol").getValue().toString();
                    password = passw_;
                    txtMessageEnviar.setText("Bienvenido, "+nombr_+"!");
                    idUsuario = nombr_;

                    if(!tipo.equals("administrador")){
                        btnUsuarios.setEnabled(false);
                        btnUsuarios.setVisibility(View.INVISIBLE);
                    }else{
                        btnUsuarios.setEnabled(true);
                        btnUsuarios.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //VGalidamos que sea un usuario valido ANterior
        /*InspeccionesDbHelper dbHelper = new InspeccionesDbHelper(SmsActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] columns = {
                InspeccionContract.UsuariosEntry.ID,
                InspeccionContract.UsuariosEntry.NOMBRES_V,
                InspeccionContract.UsuariosEntry.PASS____V
        };
        String selection = InspeccionContract.UsuariosEntry.ID + " = ?" ;
        String[] selectionArgs = {userId};
        Cursor cursor = db.query(InspeccionContract.UsuariosEntry.TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,              //group the rows
                null,               //filter by row groups
                null);             //The sort order
        int numeroFilas = cursor.getCount();
        if(numeroFilas > 0) {
            if(cursor.moveToFirst() == true) {
                String usuarioLoguiado = cursor.getString(1);
                password = cursor.getString(2);
                txtMessageEnviar.setText("Bienvenido, "+usuarioLoguiado+"!");
            }
        }
        db.close();*/
    }


    /*Segun la guia de desarrolladores de google esta es la manera correcta*/
    class getDatos extends AsyncTask<String, String, BufferedReader>{

        @Override
        protected void onPostExecute(BufferedReader res) {
            mostrarProgressBar(res, res);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("Pruebas ::> ", values[0]);
        }

        @Override
        protected BufferedReader doInBackground(String... strings) {
            BufferedReader in = null;
            try{

                //URL url = new URL("http://lapolla.com.co/images/numeros.txt");

                URL url = new URL(strings[0]);
                in = new BufferedReader(new InputStreamReader(url.openStream()));

                //SmsManager sms = SmsManager.getDefault();
                //ArrayList messagePart =  sms.divideMessage(mensage);

                if(!pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) &&
                        !pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)){
                    Toast.makeText(SmsActivity.this, "Lo sentimos, tu dispositivo probablemente no pueda enviar SMS...", Toast.LENGTH_SHORT).show();
                }else{
                    //in.close();
                }



            }catch (MalformedURLException e) {

            } catch (IOException e) {

            }
            return in;
        }


    }

    private void llenarArrayList(){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    ArrayList<String> comboUrls = new ArrayList<>();
                    comboUrls.add( "Seleccione");
                    uidUrl = new String[200];
                    uidUrl[0] = "0";
                    int i = 1;
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        comboUrls.add( ds.child("nombre").getValue().toString());
                        uidUrl[i] = ds.getKey();
                        i++;
                    }
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(SmsActivity.this, android.R.layout.simple_spinner_item, comboUrls);
                    txtUrlCargar.setAdapter(adapter);

                    //UrlData data = new UrlData(ds.getKey().toString(), ds.child("nombre").getValue().toString(), ds.child("primerNumero").getValue().toString(), ds.child("segundoNumero").getValue().toString(), ds.child("url").getValue().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtUrlCargar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dbRef = FirebaseDatabase.getInstance().getReference();
                dbRef.child("Url").child(uidUrl[i]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {
                        if(ds.exists()){
                            //Toast.makeText(SmsActivity.this, "Nombre : "+  ds.child("nombre").getValue().toString() + " Primer Numero "+  ds.child("primerNumero").getValue().toString() + " Ultimo Numero "+  ds.child("segundoNumero").getValue().toString() + " id "+ ds.getKey(), Toast.LENGTH_LONG).show();
                            urlACargar = ds.child("url").getValue().toString();
                            idUrlNombre = ds.getKey();
                            data = new UrlData(ds.getKey(), ds.child("nombre").getValue().toString(), ds.child("primerNumero").getValue().toString(), ds.child("segundoNumero").getValue().toString(), ds.child("url").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
