package com.example.examenandroid.interactors;

import android.content.Context;

import com.example.examenandroid.interfaces.Ubicaciones;

public class UbicacionesInteractor implements Ubicaciones.Interactor {

    private Ubicaciones.Presenter presenter;

    public UbicacionesInteractor(Ubicaciones.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getInitInteractor(Context context) {

    }
}
