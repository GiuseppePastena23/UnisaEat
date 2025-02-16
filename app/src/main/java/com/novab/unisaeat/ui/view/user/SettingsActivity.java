package com.novab.unisaeat.ui.view.user;

import static com.novab.unisaeat.ui.util.Utilities.showAlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.util.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {

    private boolean biometric;
    private boolean login;


    private Button userInfoButton;
    private CheckBox biometricCheckbox;
    private CheckBox loginCheckbox;

    private Button aboutAppButton;
    private Button logoutButton;

    private String currentLanguage;

    private void associateUI() {
        userInfoButton = findViewById(R.id.user_info_btn);
        biometricCheckbox = findViewById(R.id.biometric_checkbox);
        loginCheckbox = findViewById(R.id.login_checkbox);

        aboutAppButton = findViewById(R.id.dev_info_btn);
        logoutButton = findViewById(R.id.logout_btn);

        initializeButtons();
        intializeCheckbox();

    }

    private void initializeButtons() {
        userInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        });
        aboutAppButton.setOnClickListener(v -> {
            showAlertDialog(this, "UnisaEat v1.0",
                    "Developed with ❤️ by:\n\n" +
                            "Giuseppe Pastena\t[05121/18169]\nhttps://github.com/GiuseppePastena23" + "\n\n" +
                            "Pasquale Muraca\t[05121/16807]\nhttps://github.com/PasqualeMuraca" + "\n"
            );
        });
        logoutButton.setOnClickListener(v -> {
            SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
            sharedPreferencesManager.clearData();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
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
