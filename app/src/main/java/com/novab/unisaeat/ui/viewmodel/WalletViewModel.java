package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.repository.TransactionRepository;

import java.util.List;

import lombok.Getter;

public class WalletViewModel extends AndroidViewModel {
    private TransactionRepository transactionRepository;

    @Getter
    private MutableLiveData<List<Transaction>> transactionsLiveData = new MutableLiveData<>();
    @Getter
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public WalletViewModel(Application application) {
        super(application);
        transactionRepository = new TransactionRepository();
    }




}
