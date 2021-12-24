package com.example.examenandroid.services;

import com.example.examenandroid.services.entities.ResultadosPeliculas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services {

    @GET("3/movie/popular")
    Call<ResultadosPeliculas> getPeliculasPopulares(@Query("api_key") String api_key, @Query("language") String language, @Query("page") String page);

}
