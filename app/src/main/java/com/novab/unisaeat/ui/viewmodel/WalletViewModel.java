package com.novab.unisaeat.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.repository.TransactionRepository;

import java.util.List;

public class WalletViewModel {
    private TransactionRepository transactionRepository;

    private MutableLiveData<List<Transaction>> transactionsLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public WalletViewModel() {
        transactionRepository = new TransactionRepository();
    }

    public void getUserTransaction(int userId) {
        transactionRepository.getUserTransaction(userId, new TransactionRepository.TransactionsCallback() {

            @Override
            public void onSuccess(List<Transaction> transactions) {
                transactionsLiveData.setValue(transactions);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }
}
