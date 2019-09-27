package com.josegiron.smspam.Clases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josegiron.smspam.R;

import java.util.ArrayList;

public class AdaptadorFirebase extends RecyclerView.Adapter<AdaptadorFirebase.ViewHolder> {

    private ArrayList<UsuariosFB> usuariosFBS;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNombreUsuario, txtEmail, txtPassword, txtRol;
        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreUsuario = itemView.findViewById(R.id.txtNombreUserCV);
            txtEmail = itemView.findViewById(R.id.txtMailUserCV);
            txtPassword = itemView.findViewById(R.id.txtPasswordUserCV);
            txtRol = itemView.findViewById(R.id.txtRolUserCV);
        }
    }

    public AdaptadorFirebase(ArrayList<UsuariosFB> usuariosFBS){
        this.usuariosFBS = usuariosFBS;
    }

    @Override
    public int getItemCount() {
        return usuariosFBS.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.usuarios_card, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtEmail.setText("C : "+usuariosFBS.get(i).getUsu_mail());
        viewHolder.txtNombreUsuario.setText("N : "+usuariosFBS.get(i).getUsu_nombres());
        viewHolder.txtPassword.setText("P : "+usuariosFBS.get(i).getUsu_password());
        viewHolder.txtRol.setText("R : "+usuariosFBS.get(i).getUsu_rol());
    }

}
