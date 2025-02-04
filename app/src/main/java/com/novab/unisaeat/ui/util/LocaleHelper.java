package com.novab.unisaeat.ui.util;

import android.content.Context;
import android.content.SharedPreferences;

public class LocaleHelper {
    private static final String PREFS_NAME = "languagePrefs";
    private static final String KEY_LANGUAGE = "language";

    public static void setLanguage(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_LANGUAGE, "en"); // default to English
    }
}