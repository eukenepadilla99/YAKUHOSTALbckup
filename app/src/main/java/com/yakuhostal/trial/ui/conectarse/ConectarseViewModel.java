package com.yakuhostal.trial.ui.conectarse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConectarseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConectarseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is conectarse fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}