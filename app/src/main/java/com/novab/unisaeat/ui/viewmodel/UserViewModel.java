package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;
import android.util.Log;

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
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();

    public UserViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public void login(String email, String password) {
        isLoadingLiveData.setValue(true);
        userRepository.login(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                isLoadingLiveData.setValue(false);
                sharedPreferencesManager.saveUser(user);
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String errorMessage) {
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
        isLoadingLiveData.setValue(true);
        userRepository.getUserById(id, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                isLoadingLiveData.setValue(false);
                userLiveData.setValue(user);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public void getUser() {
        String email = sharedPreferencesManager.getUser().getEmail();
        String password = sharedPreferencesManager.getUser().getPassword();
        login(email, password);
    }

    public LiveData<User> getUserLiveData() {
        Log.d("TAG", "getUserLiveData: " + userLiveData.getValue());
        return userLiveData;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    private void clearLiveData() {
        userLiveData.setValue(null);
        errorLiveData.setValue(null);
    }

}
