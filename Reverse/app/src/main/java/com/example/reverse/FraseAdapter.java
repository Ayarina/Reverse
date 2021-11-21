package com.example.reverse;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reverse.ui.home.HomeFragment;

import java.util.ArrayList;

//Comunicación entre fragments y el recyclerview

public class FraseAdapter extends RecyclerView.Adapter<FraseAdapter.ViewHolder> {

    private ArrayList<Object> frases;
    private TinyDB tinyDB;

    public FraseAdapter(ArrayList<Object> frases){
        this.frases = frases;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //asignamos el frase_layout al recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frase_layout, parent, false);
        tinyDB = new TinyDB(parent.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Manejo de cada frase_layout individualmente, se podrá acceder a todos los campos definidos en
        //ViewHolder mediante el parametro holder. Con position se puede acceder a cada usuario
        //replace the contents of the view con el holder, que es basicamente la vista del frase_layout
        //obtenemos el usuario de la BD
        Frase frase = (Frase) frases.get(position);
        //Seteamos los datos del usuario añadido al crearse.

        holder.frase.setText(frase.getFrase());
        holder.score.setText(frase.getPuntuacion());
        holder.tiempo.setText(String.valueOf(frase.getTiempo()));
        //boton
        holder.jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enviar a la actividad jugar
                Intent intent = new Intent(v.getContext(), Jugar.class);
                intent.putExtra("fraseJugar", frase);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() { //num de frases agregadas
        return frases.size();
    }

    /*
    public void removeAt(int position){
        //Notificamos al recycler
        frases.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, frases.size());
        notifyItemRangeChanged(position, frases.size());
        tinyDB.putListObject("FrasesData", frases);
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Comunicacion directa con frase_layout
        //private Type Elementos del layout;
        private TextView frase, score, tiempo; //frase_tarjeta
        private Button jugar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Le asignamos los elementos del frase_layout
            //layoutElement = itemView.findViewById(R.id.elementId);
            frase = itemView.findViewById(R.id.frase_tarjeta);
            score = itemView.findViewById(R.id.score_tarjeta);
            tiempo = itemView.findViewById(R.id.tiempo_tarjeta);
            jugar = itemView.findViewById(R.id.jugar_tarjeta);
        }
    }


}
