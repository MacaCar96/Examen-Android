package com.example.examenandroid.views.ui.Ubicaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UbicacionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UbicacionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Vista de Ubicaiones - Pendiente");
    }

    public LiveData<String> getText() {
        return mText;
    }
}