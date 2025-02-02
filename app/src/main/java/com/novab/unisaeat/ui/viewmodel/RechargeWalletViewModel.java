package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.repository.TransactionRepository;

public class RechargeWalletViewModel extends AndroidViewModel {
    private TransactionRepository transactionRepository;

    private MutableLiveData<String> resultLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();


    public RechargeWalletViewModel(Application application) {
        super(application);
    }

    public void doTransaction(int userId, int amount, String mode) {
        transactionRepository.doTransaction(userId, amount, mode, new TransactionRepository.WalletRechargeCallback() {

            @Override
            public void onSuccess(String result) {
                resultLiveData.setValue(result);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public LiveData<String> getResultLiveData() {
        return resultLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
