package com.novab.unisaeat.data.api;

import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.data.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("/login")
    Call<User> login(@Body Map<String, String> userData);

    @GET("/orders")
    Call<List<Transaction>> getOrders();

    @GET("/user")
    Call<User> getUserById(@Query("user_id") int id);

    @GET("/getUserTransactions")
    Call<List<Transaction>> getUserTransactions(@Query("user_id") int userId);

    @POST("/doTransaction")
    Call<Void> doTransaction(@Body Map<String, String> transactionData);

    @GET("/getDay")
    Call<String> getDay();
}
