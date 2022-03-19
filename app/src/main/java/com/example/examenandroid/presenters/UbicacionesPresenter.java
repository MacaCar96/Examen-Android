package com.example.examenandroid.presenters;

import android.content.Context;

import com.example.examenandroid.interactors.UbicacionesInteractor;
import com.example.examenandroid.interfaces.Ubicaciones;
import com.example.examenandroid.services.entities.Pelicula;

import java.util.List;

public class UbicacionesPresenter implements Ubicaciones.Presenter {

    private Ubicaciones.View view;
    private Ubicaciones.Interactor interactor;

    public UbicacionesPresenter(Ubicaciones.View view) {
        this.view = view;
        interactor = new UbicacionesInteractor(this);
    }

    @Override
    public void showResultPresenter(List<Pelicula> result, int size) {

    }

    @Override
    public void showErrorPresenter(String result) {

    }

    @Override
    public void getInitPresenter(Context context) {

    }
}
