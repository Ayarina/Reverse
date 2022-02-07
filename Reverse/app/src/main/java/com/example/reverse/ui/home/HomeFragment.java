package com.example.reverse.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reverse.models.Frase;
import com.example.reverse.adapter.FraseAdapter;
import com.example.reverse.R;
import com.example.reverse.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private FraseAdapter fraseAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Object> frases = new ArrayList<>();

    private DatabaseReference myRef;

    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        myRef = FirebaseDatabase.getInstance("https://reverse-f3fee-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Inicializamos el adaptador
        fraseAdapter = new FraseAdapter(frases);

        //Configuramos el RecyclerView con el UserAdapter como su controlador
        recyclerView = (RecyclerView) root.findViewById(R.id.contact_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(fraseAdapter);

        ValueEventListener fraseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Frase frase;
                frases.clear();
                for(DataSnapshot fraseSnapshot: dataSnapshot.getChildren()){
                    frase = new Frase(fraseSnapshot.child("frase").getValue(String.class));
                    frases.add(frase);
                    fraseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.child("Frases").addValueEventListener(fraseListener);

        //Configuración del SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById( R.id.swiperefreshlayout ) ;


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {@Override
        public void onRefresh() {
            fraseAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}