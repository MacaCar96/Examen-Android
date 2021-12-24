package com.example.examenandroid.views.ui.Peliculas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenandroid.R;
import com.example.examenandroid.VariablesEntorno;
import com.example.examenandroid.adapters.PeliculasAdapter;
import com.example.examenandroid.interfaces.Peliculas;
import com.example.examenandroid.services.ServiceAdapter;
import com.example.examenandroid.services.Services;
import com.example.examenandroid.services.entities.ResultadosPeliculas;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeliculasFragment extends Fragment {

    private PeliculasViewModel peliculasViewModel;
    /*private Services services;*/


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        peliculasViewModel = ViewModelProviders.of(this).get(PeliculasViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        final LinearLayout linearLayoutCargandoDatos = root.findViewById(R.id.linearLayoutCargadoDatos);

        peliculasViewModel.getPeliculas().observe(getViewLifecycleOwner(), new Observer<ResultadosPeliculas>() {
            @Override
            public void onChanged(ResultadosPeliculas resultadosPeliculas) {
                if (!resultadosPeliculas.getResults().isEmpty()) {
                    linearLayoutCargandoDatos.setVisibility(View.GONE);

                    PeliculasAdapter peliculasAdapter = new PeliculasAdapter(resultadosPeliculas.getResults(), root.getContext());
                    RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    recyclerView.setAdapter(peliculasAdapter);

                } else {
                    linearLayoutCargandoDatos.setVisibility(View.GONE);

                    textView.setText("Algo fallo");
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }
}