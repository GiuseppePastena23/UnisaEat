package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.repository.TransactionRepository;
import com.novab.unisaeat.data.util.SharedPreferencesManager;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {
    SharedPreferencesManager sharedPreferencesManager =
            new SharedPreferencesManager(getApplication());

    private final TransactionRepository transactionRepository;
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();


    public TransactionViewModel(Application application) {
        super(application);
        transactionRepository = new TransactionRepository();
    }

    // ORDERS FOR EMPLOYEE
    private final MutableLiveData<List<Transaction>> ordersLiveData = new MutableLiveData<>();
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
    private final MutableLiveData<String> transactionOutcome = new MutableLiveData<>();
    public void doTransaction(int userId, float amount, String mode) {
        transactionRepository.doTransaction(userId, amount, mode, new TransactionRepository.WalletRechargeCallback() {
            @Override
            public void onSuccess(String result) {
                transactionOutcome.setValue(result);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
            }
        });
    }

    public void doTransaction(float amount, String mode) {
        doTransaction(sharedPreferencesManager.getUser().getId(), amount, mode);
    }

    public LiveData<String> getTransactionOutcome() {
        return transactionOutcome;
    }


    // GET USER TRANSACTIONS
    private final MutableLiveData<List<Transaction>> transactionsLiveData = new MutableLiveData<>();

    private void getUserTransactions(int userId) {
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

    /**
     * Fetches transactions for the current logged user from the repository.
     */
    public void getUserTransactions() {
        getUserTransactions(sharedPreferencesManager.getUser().getId());
    }
    public LiveData<List<Transaction>> getTransactionsLiveData() {
        return transactionsLiveData;
    }


    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
