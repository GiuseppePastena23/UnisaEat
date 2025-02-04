package com.novab.unisaeat.data.util;

import android.content.Context;
import android.content.SharedPreferences;

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
        user.setEmail(sharedPreferences.getString("email", ""));
        user.setStatus(sharedPreferences.getString("status", ""));
        user.setPhone(sharedPreferences.getString("phone", ""));
        user.setCredit(sharedPreferences.getFloat("credit", 0));
        return user;
    }

    public void saveBiometricCheckbox(boolean isChecked) {
        editor.putBoolean("biometric", isChecked);
        editor.apply();
    }

    public boolean getBiometricCheckbox() {
        return sharedPreferences.getBoolean("biometric", false);
    }


    public void saveLogin(boolean login) {
        editor.putBoolean("login", login);
        editor.apply();
    }

    public boolean getLogin() {
        return sharedPreferences.getBoolean("login", true);
    }

    public void clearData() {
        editor.clear();
        editor.apply();
    }
}
