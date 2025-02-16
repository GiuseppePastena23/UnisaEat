package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    private UserViewModel userViewModel;
    private EditText emailEditText, passwordEditText;
    SharedPreferencesManager sharedPreferencesManager;
    private CheckBox showPasswordCheckBox;
    private CheckBox rememberMeCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        User user = sharedPreferencesManager.getUser();
        boolean biometric = sharedPreferencesManager.getBiometricCheckbox();

        if (user != null) {
            if (!biometric) {
                goToHome();
            } else {
                checkAndAuthenticate();
            }
        } else {
            setContentView(R.layout.activity_login);
            associateUI();
        }
    }

    private void associateUI() {
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);

        Button registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        Button loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(v -> onLoginClick());

        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,
                        user.getStatus().equals("employee") ? HomeEmployeeActivity.class :
                                HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

            }
        });

        showPasswordCheckBox = findViewById(R.id.show_password_checkbox);
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // If the checkbox is checked, show the password
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {

                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Move the cursor to the end of the text
            passwordEditText.setSelection(passwordEditText.getText().length());
        });

    }

    public void onLoginClick() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (rememberMeCheckBox.isChecked()) {
            sharedPreferencesManager.saveLogin(true);
        } else {
            sharedPreferencesManager.saveLogin(false);
        }

        if (!email.isEmpty() && !password.isEmpty()) {

            String hashedPassword = Utilities.hash(password);
            userViewModel.login(email, hashedPassword);
        } else {
            Toast.makeText(this, getString(R.string.both_email_password_error),
                    Toast.LENGTH_SHORT).show();
        }
    }


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
                goToHome();  // Vai alla schermata principale dopo il successo dell'autenticazione biometrica
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                setContentView(R.layout.activity_login);
                associateUI();
                Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                setContentView(R.layout.activity_login);
                associateUI();
                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
        biometricPrompt.authenticate(buildBiometricPrompt());
    }


    private void goToHome() {
        userViewModel.getUser();
        userViewModel.getUserLiveData().observe(this, user -> {
            if(user.getPassword().equals(sharedPreferencesManager.getUser().getPassword())){
                Intent intent = new Intent(this,
                        user.getStatus().equals("employee") ? HomeEmployeeActivity.class :
                                HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                return;
            }

        });
    }
}
