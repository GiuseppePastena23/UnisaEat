package com.novab.unisaeat.data.api;

import com.novab.unisaeat.data.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/login")
    Call<User> login(@Body Map<String, String> userData);
}
