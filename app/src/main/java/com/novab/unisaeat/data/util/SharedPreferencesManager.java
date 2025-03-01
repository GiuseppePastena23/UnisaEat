package com.novab.unisaeat.data.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.novab.unisaeat.data.model.User;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "UnisaEatPreferences";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(User user) {
        if (user == null) {
            return;
        }

        editor.putInt("id", user.getId());
        editor.putString("cf", user.getCf());
        editor.putString("name", user.getName());
        editor.putString("surname", user.getSurname());
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.putString("status", user.getStatus());
        editor.putString("phone", user.getPhone());
        editor.putFloat("credit", user.getCredit());
        editor.apply();
    }

    public User getUser() {
        User user = new User();
        if (sharedPreferences.getInt("id", -1) == -1) {
            return null;
        }

        user.setId(sharedPreferences.getInt("id", -1));
        user.setCf(sharedPreferences.getString("cf", ""));
        user.setName(sharedPreferences.getString("name", ""));
        user.setSurname(sharedPreferences.getString("surname", ""));
        user.setPassword(sharedPreferences.getString("password", ""));
        user.setEmail(sharedPreferences.getString("email", ""));
        user.setStatus(sharedPreferences.getString("status", ""));
        user.setPhone(sharedPreferences.getString("phone", ""));
        user.setCredit(sharedPreferences.getFloat("credit", 0));
        return user;
    }

    public void saveLastTransaction(int idTransaction) {
        editor.putInt("idTransaction", idTransaction);
        boolean success = editor.commit();
    }

    public int getLastTransaction() {
        return sharedPreferences.getInt("idTransaction", -1);
    }

    public float getUserCredit() {
        return sharedPreferences.getFloat("credit", -1f);
    }


    public void saveAutoLogin(boolean login) {
        editor.putBoolean("login", login);
        editor.apply();
    }

    public boolean getAutoLogin() {
        Log.d("TAG", "getAutoLogin: " + sharedPreferences.getBoolean("login", false));
        return sharedPreferences.getBoolean("login", false);
    }


    public void saveBiometricAuth(boolean isChecked) {
        editor.putBoolean("biometric", isChecked);
        editor.apply();
    }

    public boolean getBiometricAuth() {
        return sharedPreferences.getBoolean("biometric", false);
    }

    public void saveLoggedIn(boolean isLoggedIn) {
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    public boolean getLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }


    public void clearData() {
        editor.clear();
        editor.apply();
    }
}
