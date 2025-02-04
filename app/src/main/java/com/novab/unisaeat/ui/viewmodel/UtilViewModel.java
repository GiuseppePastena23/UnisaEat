package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.repository.UtilRepository;

public class UtilViewModel extends AndroidViewModel {

    private final UtilRepository utilRepository;

    private final MutableLiveData<String> imageBase64LiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public UtilViewModel(Application application) {
        super(application);
        utilRepository = new UtilRepository();
    }

    public void getMenu(int day) {
        utilRepository.getMenu(day, new UtilRepository.MenuCallback() {
            @Override
            public void onSuccess(String imgBase64) {
                imageBase64LiveData.setValue(imgBase64);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public LiveData<String> getImageBase64LiveData() {
        return imageBase64LiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

}
