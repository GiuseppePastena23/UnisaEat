package com.novab.unisaeat.data.repository;

import android.util.Log;

import com.novab.unisaeat.data.api.ApiService;
import com.novab.unisaeat.data.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UtilRepository {
    private final ApiService apiService;

    public UtilRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public void getMenu(int day, final MenuCallback callback) {
        apiService.getMenu(day).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("UtilRepository", "Get menu failed: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    public interface MenuCallback {
        void onSuccess(String imgBase64);

        void onError(String errorMessage);
    }
}
