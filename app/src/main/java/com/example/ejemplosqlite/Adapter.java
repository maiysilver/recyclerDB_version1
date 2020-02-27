package com.example.ejemplosqlite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.PersonasViewHolder> {

    ArrayList<Contactos> listaContactos;

    public Adapter(ArrayList<Contactos> listaContactos) {
        this.listaContactos = listaContactos;
    }

    @Override
    public PersonasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,null,false);
        return new PersonasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonasViewHolder holder, int position) {
        holder.documento.setText(listaContactos.get(position).getId().toString());
        holder.nombre.setText(listaContactos.get(position).getNombre());
        holder.telefono.setText(listaContactos.get(position).getTelefono());
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public class PersonasViewHolder extends RecyclerView.ViewHolder {

        TextView documento,nombre,telefono;

        public PersonasViewHolder(View itemView) {
            super(itemView);
            documento = (TextView) itemView.findViewById(R.id.textId);
            nombre = (TextView) itemView.findViewById(R.id.textNombre);
            telefono = (TextView) itemView.findViewById(R.id.textTelefono);
        }
    }

}
