package com.example.examenandroid.views.ui.Peliculas;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examenandroid.interfaces.Peliculas;
import com.example.examenandroid.presenters.PeliculasPresenter;
import com.example.examenandroid.services.entities.ResultadosPeliculas;

public class PeliculasViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<ResultadosPeliculas> peliculas;

    //private Peliculas.Presenter presenter;
    private MutableLiveData<Context> context;

    private MutableLiveData<String> msgError;


    public PeliculasViewModel() {
        context = new MutableLiveData<>();
        mText = new MutableLiveData<>();
        peliculas = new MutableLiveData<>();
        msgError = new MutableLiveData<>();

        mText.setValue("Página de películas");

        /*presenter = new PeliculasPresenter(this);
        presenter.getDataPeliculasPresenter(context.getValue());*/


    }

    public LiveData<String> getText() {
        return mText;
    }

    /*@Override
    public void showResultView(ResultadosPeliculas result) {
        peliculas.setValue(result);
    }

    @Override
    public void showErrorView(String result) {
        msgError.setValue(result);
    }
*/
    public LiveData<ResultadosPeliculas> getPeliculas() { return peliculas; }
    public LiveData<String> getMsgError() { return msgError; }
}