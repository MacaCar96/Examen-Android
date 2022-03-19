package com.example.examenandroid.views.ui.Ubicaciones;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.examenandroid.R;
import com.example.examenandroid.interfaces.Ubicaciones;
import com.example.examenandroid.presenters.UbicacionesPresenter;
import com.example.examenandroid.services.entities.Ubicacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UbicacionesFragment extends Fragment implements Ubicaciones.View {

    private Ubicaciones.Presenter presenter;

    LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private Location myLocation;

    private final int TIEMPO_TAREA = 300000;
    private Handler handler = new Handler(); // En esta zona creamos el objeto Handler

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ubicaciones, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

        presenter = new UbicacionesPresenter(this);

        initFirebase();
        createLocationRequest();
        createLocationCallback();
        verificarPermiso();
        ejecutarTarea();

        return root;
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void ejecutarTarea() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                registrarUbicacion();
                handler.postDelayed(this, TIEMPO_TAREA);
            }
        }, TIEMPO_TAREA);
    }

    private void registrarUbicacion() {
        String currentDateandTime = simpleDateFormat.format(new Date());

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setId(UUID.randomUUID().toString());
        ubicacion.setFecha(currentDateandTime);
        ubicacion.setLatitud("" + myLocation.getLatitude());
        ubicacion.setLogitud("" + myLocation.getLongitude());

        Log.i("LocationGPS-Firebase", "-------------------------------------------------------------------------");
        Log.i("LocationGPS-Firebase", "Firebase Tiempo: " + currentDateandTime);
        Log.i("LocationGPS-Firebase", "Firebase Latitud: " + myLocation.getLatitude());
        Log.i("LocationGPS-Firebase", "Firebase Longitud: " + myLocation.getLongitude());
        Log.i("LocationGPS-Firebase", "-------------------------------------------------------------------------");

        databaseReference.child("Ubicaciones").child(currentDateandTime).setValue(ubicacion);
        Toast.makeText(getActivity(), "Ubicacion registrada: " + currentDateandTime, Toast.LENGTH_SHORT).show();

    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult != null) {
                    for (int i = 0; i < locationResult.getLocations().size(); i++) {
                        if (i == 0) {
                            myLocation = locationResult.getLocations().get(i);

                            String currentDateandTime = simpleDateFormat.format(new Date());

                            Log.i("LocationGPS", "Local Tiempo: " + currentDateandTime);
                            Log.i("LocationGPS", "Local Latitud: " + myLocation.getLatitude());
                            Log.i("LocationGPS", "Local Longitud: " + myLocation.getLongitude());
                            return;
                        }
                    }

                }

            }
        };
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(15000);
        //locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void verificarPermiso() {

        ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {

                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if (fineLocationGranted != null && fineLocationGranted) {
                        // Precise location access granted.
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted.
                    } else {
                        // No location access granted.
                    }

                }
        );

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        } else {
            getCoordenadas();
        }



    }

    @SuppressLint("MissingPermission")
    private void getCoordenadas() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                String currentDateandTime = simpleDateFormat.format(new Date());

                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setId(UUID.randomUUID().toString());
                ubicacion.setFecha(currentDateandTime);
                ubicacion.setLatitud("" + location.getLatitude());
                ubicacion.setLogitud("" + location.getLongitude());


                Log.i("LocationGPS-Firebase", "-------------------------------------------------------------------------");
                Log.i("LocationGPS-Firebase", "Firebase Tiempo unica: " + currentDateandTime);
                Log.i("LocationGPS-Firebase", "Firebase Latitud unica: " + location.getLatitude());
                Log.i("LocationGPS-Firebase", "Firebase Longitud unica: " + location.getLongitude());
                Log.i("LocationGPS-Firebase", "-------------------------------------------------------------------------");
                databaseReference.child("Ubicaciones").child(currentDateandTime).setValue(ubicacion);
                Toast.makeText(getActivity(), "Ubicacion registrada: " + currentDateandTime, Toast.LENGTH_SHORT).show();
            }
        });

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());


    }


    @Override
    public void showResultView(String result) {

    }

    @Override
    public void showErrorView(String result) {

    }
}