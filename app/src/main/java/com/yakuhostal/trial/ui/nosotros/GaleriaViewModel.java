package com.yakuhostal.trial.ui.nosotros;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GaleriaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GaleriaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is galeria fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}