package com.novab.unisaeat.data.repository;

import android.util.Log;

import com.novab.unisaeat.data.api.ApiService;
import com.novab.unisaeat.data.api.RetrofitClient;
import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.util.DayInfo;

import java.util.List;
import java.util.Map;

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

    public void doTransaction(int userId, float amount, String mode, final WalletRechargeCallback callback) {
        Map<String, String> transactionData =
                Map.of(
                        "user_id", String.valueOf(userId),
                        "amount", String.valueOf(amount),
                        "mode", mode
                );

        apiService.doTransaction(transactionData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("TransactionRepository", response.message());
                    callback.onSuccess(response.message());
                } else {
                    Log.e("TransactionRepository", "Transaction failed: " + response.code());
                    callback.onError("Transaction failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TransactionRepository", "Network request failed", t);
            }
        });
    }

    public void getDay(DayCallback callback) {
        Call<DayInfo> call = apiService.getDay();
        call.enqueue(new Callback<DayInfo>() {
            @Override
            public void onResponse(Call<DayInfo> call, Response<DayInfo> response) {
                if (response.isSuccessful()) {
                    Log.d("TransactionRepository", response.message());
                    callback.onSuccess(response.body());
                } else {
                    Log.e("TransactionRepository", "Retrieve Day failed: " + response.code());
                    callback.onError("Failed to get day");
                }
            }

            @Override
            public void onFailure(Call<DayInfo> call, Throwable t) {
                Log.e("TransactionRepository", "Network request failed", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface DayCallback {
        void onSuccess(DayInfo dayInfo);  // Provide the day when the request is successful

        void onError(String errorMessage);  // Handle error cases
    }

    public interface OrdersCallback {
        void onSuccess(List<Transaction> transactions);  // Provide the list of transactions when the request is successful
        void onError(String errorMessage);  // Handle error cases
    }

    public interface TransactionsCallback {
        void onSuccess(List<Transaction> transactions);  // Provide the list of transactions when the request is successful
        void onError(String errorMessage);  // Handle error cases
    }

    public interface WalletRechargeCallback {
        void onSuccess(String result);  // Provide the transaction when the request is successful
        void onError(String errorMessage);  // Handle error cases
    }
}
