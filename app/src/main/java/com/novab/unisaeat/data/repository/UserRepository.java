package com.novab.unisaeat.data.repository;

import android.util.Log;

import com.novab.unisaeat.data.api.ApiService;
import com.novab.unisaeat.data.api.RetrofitClient;
import com.novab.unisaeat.data.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final ApiService apiService;

    public UserRepository() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public void login(String email, String password, final LoginCallback callback) {
        Map<String, String> user = Map.of("email", email, "password", password);
        apiService.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else { // Wrong credentials
                    callback.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) { // Network error
                callback.onError(t.getMessage());
            }
        });
    }

    public void getUserById(int id, final LoginCallback callback) {
        apiService.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    Log.e("UserRepository", "Get user failed: " + response.code());
                    callback.onError("Get user failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserRepository", "Get user failed: " + t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }


    public interface LoginCallback {
        void onSuccess(User user);  // Provide the User object when login is successful

        void onError(String errorMessage);  // Handle error cases
    }
}
