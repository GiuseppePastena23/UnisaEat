package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.util.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    private boolean biometric;
    private boolean login;


    private Button userInfoButton;
    private CheckBox biometricCheckbox;
    private CheckBox loginCheckbox;
    private Spinner languageSpinner;
    private Button logoutButton;

    private String currentLanguage;

    private void associateUI() {
        userInfoButton = findViewById(R.id.user_info_btn);
        biometricCheckbox = findViewById(R.id.biometric_checkbox);
        loginCheckbox = findViewById(R.id.login_checkbox);
        languageSpinner = findViewById(R.id.language_spinner);
        logoutButton = findViewById(R.id.logout_btn);

        initializeButtons();
        intializeCheckbox();
        initializeSpinner();
    }

    private void initializeButtons() {
        userInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
        logoutButton.setOnClickListener(v -> {
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
            sharedPreferencesManager.clearData();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void intializeCheckbox() {
        biometricCheckbox.setChecked(biometric);
        loginCheckbox.setChecked(login);
        if (biometric) {
            loginCheckbox.setEnabled(false);
        }
        biometricCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            biometric = b;
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
            sharedPreferencesManager.saveBiometricCheckbox(b);
            if (b) {
                loginCheckbox.setChecked(true);
                loginCheckbox.setEnabled(false);
            } else {
                loginCheckbox.setEnabled(true);

            }
        });
        loginCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            login = b;
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
            sharedPreferencesManager.saveLogin(b);
        });
    }

    private void initializeSpinner() {
        String[] languages = getResources().getStringArray(R.array.language_codes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        languageSpinner.setAdapter(adapter);
        currentLanguage = getResources().getConfiguration().getLocales().get(0).getLanguage();
        int index = 0;
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(currentLanguage)) {
                index = i;
                break;
            }
        }
        languageSpinner.setSelection(index);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        biometric = sharedPreferencesManager.getBiometricCheckbox();
        login = sharedPreferencesManager.getLogin();
        associateUI();
    }


}
