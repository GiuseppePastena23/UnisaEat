package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.repository.UserRepository;

public class PrenotazioneViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> creditoSufficiente = new MutableLiveData<>();
    private UserRepository userRepository;

    public PrenotazioneViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public LiveData<Boolean> getCreditoSufficiente() {
        return creditoSufficiente;
    }

    public void controllaCredito(float importoPrenotazione, String idUtente) {

        userRepository.getUserById(idUtente, user -> {
            if (user != null) {
                float creditoDisponibile = user.getCredito();
                float creditoDisponibile =
                if (creditoDisponibile >= importoPrenotazione) {
                    creditoSufficiente.setValue(true);
                } else {
                    creditoSufficiente.setValue(false);
                }
            }
        });
    }
}
