package com.example.reverse.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reverse.models.Frase;
import com.example.reverse.R;
import com.example.reverse.models.Resultado;
import com.example.reverse.models.TinyDB;
import com.example.reverse.activities.Jugar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//Comunicación entre fragments y el recyclerview

public class FraseAdapter extends RecyclerView.Adapter<FraseAdapter.ViewHolder> {

    private ArrayList<Object> frases;
    private Resultado result = new Resultado();

    public FraseAdapter(ArrayList<Object> frases){
        this.frases = frases;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //asignamos el frase_layout al recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.frase_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Frase frase = (Frase) frases.get(position);
        DatabaseReference mdatabase = FirebaseDatabase.getInstance("https://reverse-f3fee-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mdatabase.child("Frases").child(frase.getFrase()).child("Usuarios").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    result = task.getResult().getValue(Resultado.class);
                }
            }
        });

        holder.frase.setText(frase.getFrase());
        holder.score.setText(String.valueOf(result.getPuntuacion()));
        holder.tiempo.setText(formatoTiempo(result));
        holder.jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enviar a la actividad jugar
                Intent intent = new Intent(v.getContext(), Jugar.class);
                intent.putExtra("fraseJugar", frase);
                v.getContext().startActivity(intent);
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enviar a la actividad jugar
                removeAt(position);
            }
        });

    }

    @Override
    public int getItemCount() { //num de frases agregadas
        return frases.size();
    }

    public void removeAt(int position){
        //Notificamos al recycler
        frases.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, frases.size());
        notifyItemRangeChanged(position, frases.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Comunicacion directa con frase_layout
        //private Type Elementos del layout;
        private Button jugar, eliminar;
        private TextView frase, score, tiempo; //frase_tarjeta

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Le asignamos los elementos del frase_layout
            //layoutElement = itemView.findViewById(R.id.elementId);
            frase = itemView.findViewById(R.id.frase_tarjeta);
            score = itemView.findViewById(R.id.score_tarjeta);
            tiempo = itemView.findViewById(R.id.tiempo_tarjeta);
            jugar = itemView.findViewById(R.id.jugar_tarjeta);
            eliminar = itemView.findViewById(R.id.eliminar_tarjeta);
        }
    }

    private String formatoTiempo(Resultado result){

        int minutos, segundos;
        String tiempo;

        segundos = (int) ((result.getTiempo()-result.getTiempo()%1000)/1000)%60;
        minutos = (int) ((((result.getTiempo()-result.getTiempo()%1000)/1000)-segundos)/60)%60;

        if (minutos < 10 && segundos < 10){
            tiempo =  "0"+minutos+":0"+segundos;
        } else if (minutos < 10){
            tiempo =  "0"+minutos+":"+segundos;
        } else if (segundos < 10){
            tiempo =  minutos+":0"+segundos;
        } else {
            tiempo = minutos+":"+segundos;
        }

        return tiempo;
    }




}
