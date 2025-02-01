package com.novab.unisaeat.data.repository;

import android.util.Log;

import com.novab.unisaeat.data.api.ApiService;
import com.novab.unisaeat.data.api.RetrofitClient;
import com.novab.unisaeat.data.model.Transaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionRepository {

    private ApiService apiService;

    public TransactionRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public void getOrders(final OrdersCallback callback) {
        apiService.getOrders().enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful()) {
                    Log.d("TransactionRepository", response.message());
                    callback.onSuccess(response.body());
                } else {
                    Log.e("TransactionRepository", "Retrieve Orders failed: " + response.code());
                    callback.onError("Failed to get orders");
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Log.e("TransactionRepository", "Network request failed", t);
            }
        });
    }

    public void getUserTransaction(int userId, final TransactionsCallback callback) {
        apiService.getUserTransactions(userId).enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful()) {
                    Log.d("TransactionRepository", response.message());
                    callback.onSuccess(response.body());
                } else {
                    Log.e("TransactionRepository", "Retrieve Orders failed: " + response.code());
                    callback.onError("Failed to get transactions");
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Log.e("TransactionRepository", "Network request failed", t);
            }
        });
    }

    public interface OrdersCallback {
        void onSuccess(List<Transaction> transactions);  // Provide the list of transactions when the request is successful
        void onError(String errorMessage);  // Handle error cases
    }

    public interface TransactionsCallback {
        void onSuccess(List<Transaction> transactions);  // Provide the list of transactions when the request is successful
        void onError(String errorMessage);  // Handle error cases
    }
}
