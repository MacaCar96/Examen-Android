package com.example.examenandroid.interfaces;

import com.example.examenandroid.services.entities.ResultadosPeliculas;

public interface Peliculas {

    interface  View {
        void showResultView(ResultadosPeliculas result);
    }

    interface Presenter {
        void showResultPresenter(ResultadosPeliculas result);
        void getDataPeliculasPresenter();
    }

    interface Interactor {
        void getDataPeliculasInteractor();
    }

}
