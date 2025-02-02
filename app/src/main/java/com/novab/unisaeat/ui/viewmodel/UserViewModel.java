package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.repository.UserRepository;
import com.novab.unisaeat.data.util.SharedPreferencesManager;

public class UserViewModel extends AndroidViewModel {

    SharedPreferencesManager sharedPreferencesManager =
            new SharedPreferencesManager(getApplication());
    private final UserRepository userRepository;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public UserViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public void login(String email, String password) {
        userRepository.login(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                sharedPreferencesManager.saveUserId(user.getId());
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String errorMessage) {
                sharedPreferencesManager.clearData();
                // set error message based on the error from the server
                errorMessage = errorMessage.toLowerCase();
                if (errorMessage.startsWith("failed to connect")) {
                    errorMessage = getApplication().getString(R.string.failed_to_connect);
                } else if (errorMessage.equals("bad request")) {
                    errorMessage = getApplication().getString(R.string.invalid_credentials);
                }
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public void getUserById(int id) {
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

    public void getUser() {
        getUserById(sharedPreferencesManager.getUserId());
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

}
