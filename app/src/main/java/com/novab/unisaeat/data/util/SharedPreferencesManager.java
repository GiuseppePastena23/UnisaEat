package com.novab.unisaeat.data.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "UnisaEatPreferences";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserId(int id) {
        editor.putInt("id", id);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt("id", -1); // if not found return -1
    }

    public void saveBiometricCheckbox(boolean isChecked) {
        editor.putBoolean("biometric", isChecked);
        editor.apply();
    }

    public boolean getBiometricCheckbox() {
        return sharedPreferences.getBoolean("biometric", false);
    }

    public void clearData() {
        editor.clear();
        editor.apply();
    }
}
