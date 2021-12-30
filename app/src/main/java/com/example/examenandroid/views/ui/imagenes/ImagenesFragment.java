package com.example.examenandroid.views.ui.imagenes;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImagenesFragment extends Fragment {

    private static final int GALLERY_INTENT = 1;
    private final static int CAMERA_INTENT = 2;

    private final String CARPETA_RAIZ = "temporal/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "imagenes";
    private String path = "";

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
                createSingleListDialog().show();
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GALLERY_INTENT);*/
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

        if (resultCode == Activity.RESULT_OK) {
            Imagen imagen;
            switch (requestCode) {
                case CAMERA_INTENT:

                    /*MediaScannerConnection.scanFile(root.getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {

                        }
                    });*/

                    Bundle extras = data.getExtras();
                    Bitmap imgBitmap = (Bitmap) extras.get("data");


                    imageUri = getImageUri(root.getContext(), imgBitmap);
                    imagen = new Imagen();
                    imagen.setNombreImagen(imageUri.getLastPathSegment());
                    imagen.setPathImagen(imageUri);
                    listaImagenes.add(imagen);

                    actualizarListaImagenes();
                    break;
                case GALLERY_INTENT:
                    if (data.getClipData() != null) {

                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            imageUri = data.getClipData().getItemAt(i).getUri();
                            //Toast.makeText(root.getContext(), imageUri.getLastPathSegment(), Toast.LENGTH_LONG).show();
                            imagen = new Imagen();
                            imagen.setNombreImagen(imageUri.getLastPathSegment());
                            imagen.setPathImagen(imageUri);
                            listaImagenes.add(imagen);
                        }

                    } else {

                        if (data.getData() != null) {
                            imageUri = data.getData();
                            //Toast.makeText(root.getContext(), imageUri.getLastPathSegment(), Toast.LENGTH_LONG).show();
                            imagen = new Imagen();
                            imagen.setNombreImagen(imageUri.getLastPathSegment());
                            imagen.setPathImagen(imageUri);
                            listaImagenes.add(imagen);
                        } else {
                            Toast.makeText(root.getContext(), "Seleccione imagenes...", Toast.LENGTH_LONG).show();
                        }

                    }

                    actualizarListaImagenes();
                    break;
            }



        }


    }

    private void actualizarListaImagenes() {
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

    public AlertDialog createSingleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = new CharSequence[2];

        items[0] = "Abrir camara";
        items[1] = "Abrir galeria";

        builder.setTitle("Seleccionar imagen")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which] == "Abrir camara") {

                            if (checkCameraPermission() == true){
                                /*File fileImage = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
                                boolean isCreada = fileImage.exists();
                                String nombreImagem = "";

                                if (isCreada == false) {
                                    isCreada = fileImage.mkdir();
                                }

                                if (isCreada == false) {
                                    nombreImagem = (System.currentTimeMillis() / 100) + ".jpg";
                                }

                                path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombreImagem;
                                File imagen = new File(path);

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    Uri contentUri = FileProvider.getUriForFile(getContext(), "com.example.examenandroid.provider", imagen);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                                    //intent.setDataAndType(contentUri, type);
                                } else {
                                    //intent.setDataAndType(Uri.fromFile(imagen), type);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));

                                }
                                //
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
                                startActivityForResult(intent, CAMERA_INTENT);*/

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAMERA_INTENT);
                            }

                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            startActivityForResult(intent, GALLERY_INTENT);
                        }
                    }
                });

        return builder.create();
    }

    private boolean checkCameraPermission() {
        boolean statusPermission = true;
        int permissionCheck = ContextCompat.checkSelfPermission(
                root.getContext(), Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para la camara!.");
            ActivityCompat.requestPermissions((Activity) root.getContext(), new String[]{Manifest.permission.CAMERA}, 225);
            statusPermission = false;
        } else {
            statusPermission = true;
            Log.i("Mensaje", "Tienes permiso para usar la camara.");
        }
        return statusPermission;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}