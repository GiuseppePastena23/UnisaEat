package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.repository.TransactionRepository;

import java.util.List;

import lombok.Getter;

public class RechargeWalletModelView extends AndroidViewModel {
    private TransactionRepository transactionRepository;

    private MutableLiveData<String> resultLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();


    public RechargeWalletModelView(Application application) {
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
