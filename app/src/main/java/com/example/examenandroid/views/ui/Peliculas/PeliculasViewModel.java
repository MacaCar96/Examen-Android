package com.example.examenandroid.views.ui.Peliculas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examenandroid.interfaces.Peliculas;
import com.example.examenandroid.presenters.PeliculasPresenter;
import com.example.examenandroid.services.entities.ResultadosPeliculas;

public class PeliculasViewModel extends ViewModel implements Peliculas.View {

    private MutableLiveData<String> mText;
    private MutableLiveData<ResultadosPeliculas> peliculas;

    private Peliculas.Presenter presenter;


    public PeliculasViewModel() {
        mText = new MutableLiveData<>();
        peliculas = new MutableLiveData<>();
        mText.setValue("Página de películas");

        presenter = new PeliculasPresenter(this);
        presenter.getDataPeliculasPresenter();


    }

    public LiveData<String> getText() {
        return mText;
    }

    @Override
    public void showResultView(ResultadosPeliculas result) {
        peliculas.setValue(result);
    }

    public LiveData<ResultadosPeliculas> getPeliculas() { return peliculas; }
}