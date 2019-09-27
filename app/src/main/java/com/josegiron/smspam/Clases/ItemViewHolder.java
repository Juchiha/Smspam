package com.josegiron.smspam.Clases;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.josegiron.smspam.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView txtNombreUsuario, txtEmail, txtPassword, txtRol;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        txtNombreUsuario = itemView.findViewById(R.id.txtNombreUserCV);
        txtEmail = itemView.findViewById(R.id.txtMailUserCV);
        txtPassword = itemView.findViewById(R.id.txtPasswordUserCV);
        txtRol = itemView.findViewById(R.id.txtRolUserCV);


    }
}
