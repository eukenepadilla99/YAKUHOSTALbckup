package com.yakuhostal.trial.ui.Reservas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReservasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReservasViewModel() {

    }

    public LiveData<String> getText() {
        return mText;
    }
}