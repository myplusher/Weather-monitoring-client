package com.example.smartspace2.ui.microcontrollers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MicrocontrollerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MicrocontrollerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}