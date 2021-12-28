package com.example.examenandroid.interactors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.examenandroid.VariablesEntorno;
import com.example.examenandroid.db.DbHelper;
import com.example.examenandroid.db.DbUtils;
import com.example.examenandroid.interfaces.Peliculas;
import com.example.examenandroid.services.ServiceAdapter;
import com.example.examenandroid.services.Services;
import com.example.examenandroid.services.entities.Pelicula;
import com.example.examenandroid.services.entities.ResultadosPeliculas;

import java.util.ArrayList;
import java.util.List;

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
    public void getDataPeliculasInteractor(Context context) {
        services = ServiceAdapter.getService().create(Services.class);

        Call<ResultadosPeliculas> call = services.getPeliculasPopulares(VariablesEntorno.API_KEY, VariablesEntorno.LANGUAGE, VariablesEntorno.PAGE);

        call.enqueue(new Callback<ResultadosPeliculas>() {
            @Override
            public void onResponse(Call<ResultadosPeliculas> call, Response<ResultadosPeliculas> response) {

                try {
                    resultadosPeliculas =  response.body();

                    DbHelper dbHelper = new DbHelper(context);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ArrayList<Pelicula> listaPeliculas = new ArrayList<>();
                    Pelicula pelicula = null;
                    Cursor cursor = null;

                    cursor = db.rawQuery("SELECT * FROM " + DbUtils.TABLE_PELICULAS, null);

                    if (cursor.moveToFirst()) {

                        do {
                            pelicula = new Pelicula();
                            pelicula.setBackdropPath(cursor.getString(1));
                            pelicula.setOriginalLanguage(cursor.getString(2));
                            pelicula.setOverview(cursor.getString(3));
                            pelicula.setReleaseDate(cursor.getString(4));
                            pelicula.setTitle(cursor.getString(5));

                            listaPeliculas.add(pelicula);
                        } while (cursor.moveToNext());

                    }

                    cursor.close();

                    if (listaPeliculas.size() > 0){
                        presenter.showResultPresenter(resultadosPeliculas.getResults(), resultadosPeliculas.getResults().size());
                        //presenter.showErrorPresenter("Las películas se estan actualizando.");
                    } else {

                        // Registramos las nuevas peliculas

                        long id = 0;
                        for (int i = 0; i < resultadosPeliculas.getResults().size(); i++) {

                            db = dbHelper.getReadableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(DbUtils.T_COLUMN_BACKDROP_PATH_PELICULAS, resultadosPeliculas.getResults().get(i).getBackdropPath());
                            values.put(DbUtils.T_COLUMN_ORIGINAL_TITLE_PELICULAS, resultadosPeliculas.getResults().get(i).getOriginalTitle());
                            values.put(DbUtils.T_COLUMN_OVERVIEW_PELICULAS, resultadosPeliculas.getResults().get(i).getOverview());
                            values.put(DbUtils.T_COLUMN_RELEASE_DATE_PELICULAS, resultadosPeliculas.getResults().get(i).getReleaseDate());
                            values.put(DbUtils.T_COLUMN_TITLE_PELICULAS, resultadosPeliculas.getResults().get(i).getTitle());

                            id = db.insert(DbUtils.TABLE_PELICULAS, null, values);
                        }

                        presenter.showResultPresenter(resultadosPeliculas.getResults(), resultadosPeliculas.getResults().size());
                        //presenter.showErrorPresenter("Registrando nuevas películas.");
                    }


                } catch (Exception ex) {
                    presenter.showErrorPresenter(ex.getMessage());
                    //Toast.makeText(root.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResultadosPeliculas> call, Throwable t) {
                presenter.showErrorPresenter(t.getMessage());
                //statusHTTP.setValue("Error");
                //textView.setText(t.getMessage());
                //Toast.makeText(root.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void getInitInteractor(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Pelicula> listaPeliculas = new ArrayList<>();
        Pelicula pelicula = null;
        Cursor cursor = null;

        cursor = db.rawQuery("SELECT * FROM " + DbUtils.TABLE_PELICULAS, null);

        if (cursor.moveToFirst()) {

            do {
                pelicula = new Pelicula();
                pelicula.setBackdropPath(cursor.getString(1));
                pelicula.setOriginalLanguage(cursor.getString(2));
                pelicula.setOverview(cursor.getString(3));
                pelicula.setReleaseDate(cursor.getString(4));
                pelicula.setTitle(cursor.getString(5));

                listaPeliculas.add(pelicula);
            } while (cursor.moveToNext());

        }

        cursor.close();

        presenter.showResultPresenter(listaPeliculas, listaPeliculas.size());
        //presenter.showErrorPresenter("Inicialización de peliculas totales");
    }
}
