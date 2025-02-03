package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.util.SharedPreferencesManager;
import com.novab.unisaeat.ui.view.employee.HomeEmployeeActivity;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.concurrent.Executor;

// TODO da aggiungere login biometrico se id gia presente, altrimenti login normale
public class LoginActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        int userId = sharedPreferencesManager.getUserId();
        boolean biometric = sharedPreferencesManager.getBiometricCheckbox();


        if (userId != -1) {
            if (!biometric) {
                goToHome();
            } else {
                checkAndAuthenticate();
            }
        }


    }

    private void associateUI() {
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_btn);
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
    }

    public void onLoginClick() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // TODO:
        // email = "m.r";
        password = "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb";

        /*!password.isEmpty()*/
        if (!email.isEmpty()) {
            userViewModel.login(email, password);
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
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                goToHome();  // Vai alla schermata principale dopo il successo dell'autenticazione biometrica
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                // FIXME: is this good?
                super.onAuthenticationError(errorCode, errString);
                setContentView(R.layout.activity_login);
                associateUI();
                Toast.makeText(LoginActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                // FIXME: is this good?
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
            Intent intent = new Intent(this,
                    user.getStatus().equals("employee") ? HomeEmployeeActivity.class :
                            HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
