package com.example.examenandroid.presenters;

import com.example.examenandroid.interactors.PeliculasInteractor;
import com.example.examenandroid.interfaces.Peliculas;
import com.example.examenandroid.services.entities.ResultadosPeliculas;

public class PeliculasPresenter implements Peliculas.Presenter {

    private Peliculas.View view;
    private Peliculas.Interactor interactor;

    public PeliculasPresenter(Peliculas.View view) {
        this.view = view;
        interactor = new PeliculasInteractor(this);
    }

    @Override
    public void showResultPresenter(ResultadosPeliculas result) {
       if (view != null) {
           view.showResultView(result);
       }
    }

    @Override
    public void getDataPeliculasPresenter() {
        if (view != null) {
            interactor.getDataPeliculasInteractor();
        }
    }
}
