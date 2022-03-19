package com.example.examenandroid.interfaces;

import android.content.Context;

import com.example.examenandroid.services.entities.Pelicula;

import java.util.List;

public interface Ubicaciones {

    interface View {
        void showResultView(String result);
        void showErrorView(String result);
    }

    interface Presenter {
        void showResultPresenter(List<Pelicula> result, int size);
        void showErrorPresenter(String result);
        //void getDataPeliculasPresenter(Context context);
        void getInitPresenter(Context context);
    }

    interface Interactor {
        //void getDataPeliculasInteractor(Context context);
        void getInitInteractor(Context context);
    }

}
