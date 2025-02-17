package com.novab.unisaeat.ui.view.user;

import static com.novab.unisaeat.ui.util.Utilities.showAlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.util.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferencesManager sharedPreferencesManager;

    private boolean biometric;
    private boolean autoLogin;

    private Button userInfoButton;
    private SwitchCompat biometricCheckbox;
    private SwitchCompat autoLoginCheckbox;

    private Button aboutAppButton;
    private Button logoutButton;

    private void associateUI() {
        userInfoButton = findViewById(R.id.user_info_btn);
        biometricCheckbox = findViewById(R.id.biometric_checkbox);
        autoLoginCheckbox = findViewById(R.id.login_checkbox);

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
            showAlertDialog(
                    this,
                    "UnisaEat v1.0",
                    "Developed with ❤️ by:\n\n" +
                            "Giuseppe Pastena\t[05121/18169]\nhttps://github.com/GiuseppePastena23" + "\n\n" +
                            "Pasquale Muraca\t[05121/16807]\nhttps://github.com/PasqualeMuraca" + "\n\n" +
                            "App Logo made by: Davide(Cipo)\n",
                    (dialogInterface, i) -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/GiuseppePastena23/UnisaEat"));
                        startActivity(browserIntent);
                    },
                true
            );
        });
        logoutButton.setOnClickListener(v -> {
            sharedPreferencesManager.clearData();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void intializeCheckbox() {
        biometricCheckbox.setChecked(biometric);
        autoLoginCheckbox.setChecked(autoLogin);
        if (biometric) {
            autoLoginCheckbox.setEnabled(false);
            autoLoginCheckbox.setChecked(true);
        }

        biometricCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            biometric = b;
            sharedPreferencesManager.saveBiometricAuth(b);
            if (b) {
                autoLoginCheckbox.setChecked(true);
                autoLoginCheckbox.setEnabled(false);
            } else {
                autoLoginCheckbox.setEnabled(true);
            }
        });
        autoLoginCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            autoLogin = b;
            sharedPreferencesManager.saveAutoLogin(autoLogin);
            sharedPreferencesManager.saveLoggedIn(autoLogin);
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        biometric = sharedPreferencesManager.getBiometricAuth();
        autoLogin = sharedPreferencesManager.getAutoLogin();
        associateUI();
    }

}
