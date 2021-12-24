package com.example.examenandroid.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenandroid.R;
import com.example.examenandroid.services.entities.Pelicula;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PeliculasAdapter extends RecyclerView.Adapter<PeliculasAdapter.ViewHolder> {

    private List<Pelicula> data;
    private LayoutInflater layoutInflater;
    private Context context;

    public PeliculasAdapter(List<Pelicula> itemList, Context context){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.list_peliculas, null);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.bindData(data.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPortada;
        TextView textViewTitulo, textViewFecha, textViewDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewPortada = itemView.findViewById(R.id.imageViewPortada);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);

        }

        public void bindData(Pelicula pelicula) throws IOException {

            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + pelicula.getBackdropPath())
                    .into(imageViewPortada);

            ;

            textViewTitulo.setText(pelicula.getTitle());
            textViewFecha.setText(pelicula.getReleaseDate());

            if (pelicula.getOverview().length() >= 200) {
                textViewDescripcion.setText(pelicula.getOverview().substring(0, 200) + "...");
            } else {
                textViewDescripcion.setText(pelicula.getOverview());
            }



        }

    }
}
