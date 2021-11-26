package com.example.reverse.ui.home;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reverse.Frase;
import com.example.reverse.FraseAdapter;
import com.example.reverse.R;
import com.example.reverse.TinyDB;
import com.example.reverse.Usuario;
import com.example.reverse.databinding.FragmentHomeBinding;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private FraseAdapter fraseAdapter;
    private TinyDB tinyDB;
    private RecyclerView recyclerView;
    private ArrayList<Object> frases;

    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Inicializamos la base de datos
        tinyDB = new TinyDB(getContext());
        //Inicializamos el ArrayList (comprobando antes si esta vacío o no)
        if(tinyDB.getListObject("frases", Frase.class) != null)
            frases = tinyDB.getListObject("frases", Frase.class);
        else
            frases = new ArrayList<>();

        //Inicializamos el adaptador
        fraseAdapter = new FraseAdapter(frases);

        //Configuramos el RecyclerView con el UserAdapter como su controlador
        recyclerView = (RecyclerView) root.findViewById(R.id.contact_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(fraseAdapter);

        //Configuración del SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById( R.id.swiperefreshlayout ) ;


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {@Override
        public void onRefresh() {
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