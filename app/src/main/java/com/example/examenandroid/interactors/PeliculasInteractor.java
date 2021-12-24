package com.example.examenandroid.interactors;

import com.example.examenandroid.VariablesEntorno;
import com.example.examenandroid.interfaces.Peliculas;
import com.example.examenandroid.services.ServiceAdapter;
import com.example.examenandroid.services.Services;
import com.example.examenandroid.services.entities.ResultadosPeliculas;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeliculasInteractor implements Peliculas.Interactor {

    private Peliculas.Presenter presenter;
    private ResultadosPeliculas resultadosPeliculas;
    private Services services;

    public PeliculasInteractor(Peliculas.Presenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void getDataPeliculasInteractor() {
        services = ServiceAdapter.getService().create(Services.class);

        Call<ResultadosPeliculas> call = services.getPeliculasPopulares(VariablesEntorno.API_KEY, VariablesEntorno.LANGUAGE, VariablesEntorno.PAGE);

        call.enqueue(new Callback<ResultadosPeliculas>() {
            @Override
            public void onResponse(Call<ResultadosPeliculas> call, Response<ResultadosPeliculas> response) {

                try {
                    resultadosPeliculas =  response.body();
                    presenter.showResultPresenter(resultadosPeliculas);

                } catch (Exception ex) {

                    //Toast.makeText(root.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResultadosPeliculas> call, Throwable t) {
                //statusHTTP.setValue("Error");
                //textView.setText(t.getMessage());
                //Toast.makeText(root.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
}
