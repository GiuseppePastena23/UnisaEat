package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.repository.UserRepository;

public class PaymentViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public PaymentViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public void getUserById(String id) {
        userRepository.getUserById(id, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

}
