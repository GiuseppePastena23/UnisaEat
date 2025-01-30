package com.novab.unisaeat.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.repository.UserRepository;

public class LoginViewModel {

    private UserRepository userRepository;
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();


    public LoginViewModel() {
        userRepository = new UserRepository();
    }

    // Funzione per eseguire il login
    public void login(String email, String password) {

        userRepository.login(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.setValue(user);  // Se login va a buon fine, aggiorna il dato dell'utente
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);  // Gestisce eventuali errori
            }
        });
    }

    // Getter per il LiveData (che la View osserverà)
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }


}