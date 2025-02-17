package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.util.SharedPreferencesManager;
import com.novab.unisaeat.ui.util.Utilities;
import com.novab.unisaeat.ui.view.employee.HomeEmployeeActivity;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private UserViewModel userViewModel;
    private EditText emailEditText, passwordEditText;
    private CheckBox showPasswordCheckBox, rememberMeCheckBox;
    private Button loginButton, registerButton;
    private ImageView fingerprintButton;

    boolean bioAuthOutcome = false;
    boolean wantsAutoLogin = false, wantsBiometric = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        wantsAutoLogin = sharedPreferencesManager.getAutoLogin();
        wantsBiometric = sharedPreferencesManager.getBiometricAuth();

        associateUI();
        setupListeners();
        observeLogin();

        if (wantsAutoLogin) {
            if (wantsBiometric) {
                checkAndAuthenticate();
            } else {
                userViewModel.login(sharedPreferencesManager.getUser().getEmail(), sharedPreferencesManager.getUser().getPassword());

            }
        }


    }

    private void associateUI() {
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        showPasswordCheckBox = findViewById(R.id.show_password_checkbox);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);
        rememberMeCheckBox.setChecked(wantsAutoLogin);
        fingerprintButton = findViewById(R.id.fingerprint_button);
        loginButton = findViewById(R.id.login_btn);
        registerButton = findViewById(R.id.register_btn);

        User tmp = sharedPreferencesManager.getUser();
        if (tmp == null ||
                (!wantsBiometric && (tmp.getEmail().isEmpty() || tmp.getPassword().isEmpty()))) {
            fingerprintButton.setAlpha(0.1f);
            fingerprintButton.setEnabled(false);
        }
    }

    private void setupListeners() {
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            passwordEditText.setInputType(isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        });

        registerButton.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        fingerprintButton.setOnClickListener(v -> {
            checkAndAuthenticate();
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show();
            } else {
                userViewModel.login(email, Utilities.hash(password));
            }
        });

    }


    private void observeLogin() {
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                boolean rememberMe = rememberMeCheckBox.isChecked();
                sharedPreferencesManager.saveAutoLogin(rememberMe);
                sharedPreferencesManager.saveLoggedIn(rememberMe);
                sharedPreferencesManager.saveUser(user);
                Intent intent = new Intent(this, user.getStatus().equals("employee") ?
                        HomeEmployeeActivity.class : HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Login fallito!", Toast.LENGTH_SHORT).show();
            }
        });

        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // **************** BIOMETRIC AUTHENTICATION **************** //

    private void checkAndAuthenticate() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                showBiometricPrompt();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Biometric Authentication currently unavailable", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "Your device doesn't support Biometric Authentication", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "Your device doesn't have any fingerprint enrolled", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Toast.makeText(this, "Your device is not updated", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Toast.makeText(this, "Your device is not supported", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Toast.makeText(this, "Biometric status unknown", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private BiometricPrompt.PromptInfo buildBiometricPrompt() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("FingerPrint Authentication")
                .setDescription("Please place your finger on the sensor to unlock")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .setNegativeButtonText("Cancel")
                .build();
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                userViewModel.login(sharedPreferencesManager.getUser().getEmail(), sharedPreferencesManager.getUser().getPassword());
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
        biometricPrompt.authenticate(buildBiometricPrompt());
    }
}
