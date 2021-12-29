package com.example.examenandroid.services.entities;

import android.net.Uri;

public class Imagen {

    String nombreImagen;
    Uri pathImagen;

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public Uri getPathImagen() {
        return pathImagen;
    }

    public void setPathImagen(Uri pathImagen) {
        this.pathImagen = pathImagen;
    }
}
