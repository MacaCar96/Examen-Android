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
import com.example.examenandroid.presenters.PeliculasPresenter;
import com.example.examenandroid.services.ServiceAdapter;
import com.example.examenandroid.services.Services;
import com.example.examenandroid.services.entities.Pelicula;
import com.example.examenandroid.services.entities.ResultadosPeliculas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeliculasFragment extends Fragment implements Peliculas.View {

    private PeliculasViewModel peliculasViewModel;
    private Peliculas.Presenter presenter;
    private boolean statusUpdatePeliculas = false;

    /*private Services services;*/

    private View root;
    private TextView textView, textViewCargaDatos;
    private LinearLayout linearLayoutCargandoDatos;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        peliculasViewModel = ViewModelProviders.of(this).get(PeliculasViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.text_home);
        textViewCargaDatos = root.findViewById(R.id.textViewCardaDatos);
        linearLayoutCargandoDatos = root.findViewById(R.id.linearLayoutCargadoDatos);

        presenter = new PeliculasPresenter(this);
        presenter.getInitPresenter(root.getContext()); // Inicializamos la conprobación de datos locales
        //presenter.getDataPeliculasPresenter(root.getContext());

        return root;
    }

    @Override
    public void showResultView(List<Pelicula> result, int size) {
        if (!result.isEmpty()) { // En caso de que ya haya data disponible local
            linearLayoutCargandoDatos.setVisibility(View.GONE); // Ocultamos el cargador

            PeliculasAdapter peliculasAdapter = new PeliculasAdapter(result, root.getContext());
            RecyclerView recyclerView = root.findViewById(R.id.listRecyclerView);
            recyclerView.setVisibility(View.VISIBLE); // Visualizamos el RecyclerView
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
            recyclerView.setAdapter(peliculasAdapter);

            if (!statusUpdatePeliculas) {
                // actualizamos peliculas nuevas API
                statusUpdatePeliculas = true;
                presenter.getDataPeliculasPresenter(root.getContext());
            }



        } else {
            presenter.getDataPeliculasPresenter(root.getContext());
            //linearLayoutCargandoDatos.setVisibility(View.GONE);

            //textView.setText("Algo fallo");
            //textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorView(String result) {
        Toast.makeText(root.getContext(), result.toString(), Toast.LENGTH_LONG).show();

        textViewCargaDatos.setText("Algo fallo... " + result.toString());

    }
}