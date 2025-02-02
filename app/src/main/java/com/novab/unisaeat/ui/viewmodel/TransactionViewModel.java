package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.repository.TransactionRepository;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {
    private final TransactionRepository transactionRepository;
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();


    public TransactionViewModel(Application application) {
        super(application);
        transactionRepository = new TransactionRepository();
    }

    // ORDERS FOR EMPLOYEE
    private MutableLiveData<List<Transaction>> ordersLiveData = new MutableLiveData<>();
    public void getOrders() {
        transactionRepository.getOrders(new TransactionRepository.OrdersCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                ordersLiveData.setValue(transactions);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }
    public LiveData<List<Transaction>> getOrdersLiveData() {
        return ordersLiveData;

    }


    // RECHARGE WALLET
    private MutableLiveData<String> resultLiveData = new MutableLiveData<>();

    public void doTransaction(int userId, float amount, String mode) {
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


    // GET USER TRANSACTIONS
    private MutableLiveData<List<Transaction>> transactionsLiveData = new MutableLiveData<>();
    public void getUserTransaction(int userId) {
        transactionRepository.getUserTransaction(userId, new TransactionRepository.TransactionsCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                Log.d("WalletViewModel", "onSuccess: " + transactions);
                transactionsLiveData.setValue(transactions);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("WalletViewModel", "onError: " + errorMessage);
                errorLiveData.setValue(errorMessage);
            }
        });
    }
    public LiveData<List<Transaction>> getTransactionsLiveData() {
        return transactionsLiveData;
    }


    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
