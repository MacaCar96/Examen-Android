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
                            pelicula.setId(cursor.getInt(1));
                            pelicula.setBackdropPath(cursor.getString(2));
                            pelicula.setOriginalLanguage(cursor.getString(3));
                            pelicula.setOverview(cursor.getString(4));
                            pelicula.setReleaseDate(cursor.getString(5));
                            pelicula.setTitle(cursor.getString(6));

                            listaPeliculas.add(pelicula);
                        } while (cursor.moveToNext());

                    }

                    cursor.close();

                    if (listaPeliculas.size() > 0){
                        presenter.showResultPresenter(resultadosPeliculas.getResults(), resultadosPeliculas.getResults().size());
                        //presenter.showErrorPresenter("Las películas se estan actualizando.");

                        // Vamos a eliminar todas las peliculas locales que no esten en la nueva consulta
                        for (int i = 0; i < listaPeliculas.size(); i++) { // Lista de películas en SQLite
                            boolean statusEliminarPelicula = false;

                            for (int a = 0; a < resultadosPeliculas.getResults().size(); a++) { // Lista de películas desde la API
                                if (resultadosPeliculas.getResults().get(a).getId() != listaPeliculas.get(i).getId()) {
                                    // Si la película en local es diferente a todas a las películas de la nueva consulta
                                    // la vamos a eliminar

                                    statusEliminarPelicula = true;
                                }
                            }

                            if (statusEliminarPelicula) {
                                // Si es igual a true la película es eliminada
                                db = dbHelper.getWritableDatabase();
                                db.delete(DbUtils.TABLE_PELICULAS, DbUtils.T_COLUMN_ID_MOVIE_PELICULAS + " = ?", new String[]{listaPeliculas.get(i).getId().toString()});
                                //presenter.showErrorPresenter("Pelicula eliminada: " + listaPeliculas.get(i).getTitle());
                            }
                        }

                        // Vamos a actualizar las películas que quedaron después de proceso de eliminacion y registraremos las nuevas que no esten disponibles para actualizar
                        for (int i = 0; i < resultadosPeliculas.getResults().size(); i++) {
                            boolean statusActualizar = false;

                            for (int a = 0; a < listaPeliculas.size(); a++) {
                                if (resultadosPeliculas.getResults().get(i).getId() == listaPeliculas.get(a).getId()) {
                                    // Si las peliculas ya esta registrada se actualzira
                                    statusActualizar = true;
                                }
                            }

                            if (statusActualizar) {
                                // Si es true actualizamos la película
                                db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(DbUtils.T_COLUMN_BACKDROP_PATH_PELICULAS, resultadosPeliculas.getResults().get(i).getBackdropPath());
                                values.put(DbUtils.T_COLUMN_ORIGINAL_TITLE_PELICULAS, resultadosPeliculas.getResults().get(i).getOriginalTitle());
                                values.put(DbUtils.T_COLUMN_OVERVIEW_PELICULAS, resultadosPeliculas.getResults().get(i).getOverview());
                                values.put(DbUtils.T_COLUMN_RELEASE_DATE_PELICULAS, resultadosPeliculas.getResults().get(i).getReleaseDate());
                                values.put(DbUtils.T_COLUMN_TITLE_PELICULAS, resultadosPeliculas.getResults().get(i).getTitle());

                                db.update(DbUtils.TABLE_PELICULAS, values, DbUtils.T_COLUMN_ID_MOVIE_PELICULAS + " = ?", new String[]{resultadosPeliculas.getResults().get(i).getId().toString()});

                                //presenter.showErrorPresenter("Pelicula actualizada: " + resultadosPeliculas.getResults().get(i).getTitle());
                            } else {
                                // Si es false registramos la nueva película
                                db = dbHelper.getReadableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(DbUtils.T_COLUMN_ID_MOVIE_PELICULAS, resultadosPeliculas.getResults().get(i).getId());
                                values.put(DbUtils.T_COLUMN_BACKDROP_PATH_PELICULAS, resultadosPeliculas.getResults().get(i).getBackdropPath());
                                values.put(DbUtils.T_COLUMN_ORIGINAL_TITLE_PELICULAS, resultadosPeliculas.getResults().get(i).getOriginalTitle());
                                values.put(DbUtils.T_COLUMN_OVERVIEW_PELICULAS, resultadosPeliculas.getResults().get(i).getOverview());
                                values.put(DbUtils.T_COLUMN_RELEASE_DATE_PELICULAS, resultadosPeliculas.getResults().get(i).getReleaseDate());
                                values.put(DbUtils.T_COLUMN_TITLE_PELICULAS, resultadosPeliculas.getResults().get(i).getTitle());

                                db.insert(DbUtils.TABLE_PELICULAS, null, values);
                                //presenter.showErrorPresenter("Pelicula registrada: " + resultadosPeliculas.getResults().get(i).getTitle());
                            }
                        }


                    } else {

                        // Registramos las nuevas peliculas
                        presenter.showResultPresenter(resultadosPeliculas.getResults(), resultadosPeliculas.getResults().size());

                        long id = 0;
                        for (int i = 0; i < resultadosPeliculas.getResults().size(); i++) {

                            db = dbHelper.getReadableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(DbUtils.T_COLUMN_ID_MOVIE_PELICULAS, resultadosPeliculas.getResults().get(i).getId());
                            values.put(DbUtils.T_COLUMN_BACKDROP_PATH_PELICULAS, resultadosPeliculas.getResults().get(i).getBackdropPath());
                            values.put(DbUtils.T_COLUMN_ORIGINAL_TITLE_PELICULAS, resultadosPeliculas.getResults().get(i).getOriginalTitle());
                            values.put(DbUtils.T_COLUMN_OVERVIEW_PELICULAS, resultadosPeliculas.getResults().get(i).getOverview());
                            values.put(DbUtils.T_COLUMN_RELEASE_DATE_PELICULAS, resultadosPeliculas.getResults().get(i).getReleaseDate());
                            values.put(DbUtils.T_COLUMN_TITLE_PELICULAS, resultadosPeliculas.getResults().get(i).getTitle());

                            id = db.insert(DbUtils.TABLE_PELICULAS, null, values);
                        }


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
                pelicula.setId(cursor.getInt(1));
                pelicula.setBackdropPath(cursor.getString(2));
                pelicula.setOriginalLanguage(cursor.getString(3));
                pelicula.setOverview(cursor.getString(4));
                pelicula.setReleaseDate(cursor.getString(5));
                pelicula.setTitle(cursor.getString(6));

                listaPeliculas.add(pelicula);
            } while (cursor.moveToNext());

        }

        cursor.close();

        presenter.showResultPresenter(listaPeliculas, listaPeliculas.size());
        //presenter.showErrorPresenter("Inicialización de peliculas totales");
    }
}
