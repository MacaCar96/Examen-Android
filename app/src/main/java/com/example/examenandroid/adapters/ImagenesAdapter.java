package com.example.examenandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenandroid.R;
import com.example.examenandroid.services.entities.Imagen;
import com.example.examenandroid.services.entities.Pelicula;

import java.io.IOException;
import java.util.List;

public class ImagenesAdapter extends RecyclerView.Adapter<ImagenesAdapter.ViewHolder> implements View.OnClickListener {

    private List<Imagen> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private View.OnClickListener listener;

    public ImagenesAdapter(List<Imagen> itemList, Context context){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_imagenes, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNombreImagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNombreImagen = itemView.findViewById(R.id.textViewNombreImagen);
        }

        public void bindData(Imagen imagen) {
            textViewNombreImagen.setText(imagen.getNombreImagen());
        }
    }
}
