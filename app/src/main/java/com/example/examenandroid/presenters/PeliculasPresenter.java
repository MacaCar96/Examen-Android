package com.example.examenandroid.presenters;

import android.content.Context;

import com.example.examenandroid.interactors.PeliculasInteractor;
import com.example.examenandroid.interfaces.Peliculas;
import com.example.examenandroid.services.entities.Pelicula;
import com.example.examenandroid.services.entities.ResultadosPeliculas;

import java.util.List;

public class PeliculasPresenter implements Peliculas.Presenter {

    private Peliculas.View view;
    private Peliculas.Interactor interactor;

    public PeliculasPresenter(Peliculas.View view) {
        this.view = view;
        interactor = new PeliculasInteractor(this);
    }

    @Override
    public void showResultPresenter(List<Pelicula> result, int size) {
       if (view != null) {
           view.showResultView(result, size);
       }
    }

    @Override
    public void showErrorPresenter(String result) {
        if (view != null) {
            view.showErrorView(result);
        }
    }

    @Override
    public void getDataPeliculasPresenter(Context context) {
        if (view != null) {
            interactor.getDataPeliculasInteractor(context);
        }
    }

    @Override
    public void getInitPresenter(Context context) {
        if (view != null) {
            interactor.getInitInteractor(context); // Le pasamos el incialización a la clase Interactor
        }
    }
}
