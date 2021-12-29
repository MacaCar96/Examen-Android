package com.example.examenandroid.views.ui.imagenes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenandroid.R;
import com.example.examenandroid.adapters.ImagenesAdapter;
import com.example.examenandroid.services.entities.Imagen;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


public class ImagenesFragment extends Fragment {

    private static final int GALLERY_INTENT = 1;

    private View root;
    private Button buttonSeleccionarImagen, buttonSubirFoto;
    private RecyclerView recyclerViewImagenes;
    private ProgressDialog progressDialog;

    private Uri imageUri;
    private StorageReference storage;

    private List<Imagen> listaImagenes = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_imagenes, container, false);

        storage = FirebaseStorage.getInstance().getReference();
        buttonSeleccionarImagen = root.findViewById(R.id.buttonSeleccionarImagen);
        buttonSubirFoto = root.findViewById(R.id.buttonSubirFoto);
        recyclerViewImagenes = root.findViewById(R.id.recyclerViewImagenes);
        progressDialog = new ProgressDialog(root.getContext());
        progressDialog.setTitle("Subiendo fotos");
        progressDialog.setMessage("Espere por favor...");

        ImagenesAdapter imagenesAdapter = new ImagenesAdapter(listaImagenes, root.getContext());
        recyclerViewImagenes.setHasFixedSize(true);
        recyclerViewImagenes.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerViewImagenes.setAdapter(imagenesAdapter);

        buttonSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        buttonSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Subir foto al FirebaseStorage
                progressDialog.show();
                //StorageReference filePath = storage.child("imagenes");

                String mensaje = "Se subio exitosamente las imagenes";
                if (listaImagenes.size() > 1) {
                    mensaje = "Se subieron exitosamente las imagenes";
                }


                for (int i = 0; i < listaImagenes.size(); i++) {
                    StorageReference filePath = storage.child("imagenes").child(listaImagenes.get(i).getPathImagen().getLastPathSegment());

                    int finalI = i;
                    String finalMensaje = mensaje;
                    filePath.putFile(listaImagenes.get(i).getPathImagen()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if (finalI == listaImagenes.size() - 1) {
                                progressDialog.dismiss();
                                Toast.makeText(root.getContext(), finalMensaje, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
            //Toast.makeText(root.getContext(), "Foto recuperada", Toast.LENGTH_LONG).show();
            //imageUri = data.getData();

            if (data.getClipData() != null) {

                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    imageUri = data.getClipData().getItemAt(i).getUri();
                    //Toast.makeText(root.getContext(), imageUri.getLastPathSegment(), Toast.LENGTH_LONG).show();
                    Imagen imagen = new Imagen();
                    imagen.setNombreImagen(imageUri.getLastPathSegment());
                    imagen.setPathImagen(imageUri);
                    listaImagenes.add(imagen);
                }

            } else {

                if (data.getData() != null) {
                    imageUri = data.getData();
                    //Toast.makeText(root.getContext(), imageUri.getLastPathSegment(), Toast.LENGTH_LONG).show();
                    Imagen imagen = new Imagen();
                    imagen.setNombreImagen(imageUri.getLastPathSegment());
                    imagen.setPathImagen(imageUri);
                    listaImagenes.add(imagen);
                } else {
                    Toast.makeText(root.getContext(), "Seleccione imagenes...", Toast.LENGTH_LONG).show();
                }

            }

            ImagenesAdapter imagenesAdapter = new ImagenesAdapter(listaImagenes, root.getContext());
            imagenesAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(listaImagenes.get(recyclerViewImagenes.getChildAdapterPosition(view)).getPathImagen().getPath()));
                    intent.setType("image/*");
                    startActivity(intent);*/
                    //final Intent intent = new Intent(Intent.ACTION_VIEW, (Uri) listaImagenes.get(recyclerViewImagenes.getChildAdapterPosition(view)).getPathImagen().getPathSegments());

                }
            });

            recyclerViewImagenes.setHasFixedSize(true);
            recyclerViewImagenes.setLayoutManager(new LinearLayoutManager(root.getContext()));
            recyclerViewImagenes.setAdapter(imagenesAdapter);

        }


    }

}