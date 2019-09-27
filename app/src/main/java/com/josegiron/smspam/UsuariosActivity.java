package com.josegiron.smspam;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.josegiron.smspam.Clases.AdaptadorFirebase;
import com.josegiron.smspam.Clases.Usuarios;
import com.josegiron.smspam.Clases.UsuariosFB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsuariosActivity extends AppCompatActivity {
    private AlertDialog.Builder builder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        final RecyclerView recycler = findViewById(R.id.ReciclerViewUsuarios);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();



        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ArrayList<UsuariosFB> usuariosFBS = new ArrayList<>();

                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        usuariosFBS.add( new UsuariosFB(ds.getKey().toString(),  ds.child("nombre").getValue().toString(), ds.child("password").getValue().toString(),  ds.child("correo").getValue().toString(), ds.child("rol").getValue().toString()));
                    }

                    AdaptadorFirebase adaptadorFirebase = new AdaptadorFirebase(usuariosFBS);
                    recycler.setAdapter(adaptadorFirebase);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_plus:
                /*Codigo*/
                generateDialogNuevoUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void generateDialogNuevoUsuario(){
        builder = new AlertDialog.Builder( UsuariosActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View vista = inflater.inflate(R.layout.dialog_singup, null);
        final EditText passwordEditText = vista.findViewById(R.id.txtNuevoPassword);
        final EditText correoEditText   = vista.findViewById(R.id.txtNuevoCorreo);
        final EditText nombresEditText  = vista.findViewById(R.id.txtNuevoNombreUsuario);
        builder.setView(vista)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if(correoEditText.getText().length() > 1 && passwordEditText.getText().length() > 4 && nombresEditText.getText().length() > 1){
                            final FirebaseAuth mAuth =  FirebaseAuth.getInstance();
                            mAuth.createUserWithEmailAndPassword(correoEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Map<String, String> map = new HashMap<>();
                                        map.put("nombre", nombresEditText.getText().toString());
                                        map.put("password", passwordEditText.getText().toString());
                                        map.put("correo", correoEditText.getText().toString());
                                        map.put("rol", "usuario");
                                        String id = mAuth.getCurrentUser().getUid();

                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task2) {
                                                if(task2.isSuccessful()){
                                                    Toast.makeText(UsuariosActivity.this, "Usuario Creado con exito!", Toast.LENGTH_LONG).show();
                                                    dialog.cancel();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(UsuariosActivity.this, "Los campos son necesarios", Toast.LENGTH_LONG).show();
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
}
