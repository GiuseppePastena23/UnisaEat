package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.repository.UtilRepository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UtilViewModel extends AndroidViewModel {

    private final UtilRepository utilRepository;
    private final MutableLiveData<String> imagesBase64LiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();

    public UtilViewModel(Application application) {
        super(application);
        utilRepository = new UtilRepository();
    }


    private void getMenu(int day) {
        isLoadingLiveData.setValue(true);

        utilRepository.getMenu(day, new UtilRepository.MenuCallback() {
            @Override
            public void onSuccess(String imgBase64) {
                imagesBase64LiveData.setValue(imgBase64);
                isLoadingLiveData.setValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
                isLoadingLiveData.setValue(false);
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////

    public void getDayMenu(int day) {
        getMenu(day);
    }

    public LiveData<String> getImagesBase64LiveData() {
        return imagesBase64LiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }
}
