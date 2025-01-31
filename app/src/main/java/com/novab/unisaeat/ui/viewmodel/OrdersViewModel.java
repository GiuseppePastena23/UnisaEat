package com.novab.unisaeat.ui.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.repository.TransactionRepository;
import com.novab.unisaeat.data.repository.UserRepository;

import java.util.List;

public class OrdersViewModel {

    private TransactionRepository transactionRepository;
    private MutableLiveData<List<Transaction>> ordersLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();


    public OrdersViewModel() {
        transactionRepository= new TransactionRepository();
    }

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
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
