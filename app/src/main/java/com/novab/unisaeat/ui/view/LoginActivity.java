package com.novab.unisaeat.ui.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText emailEditText, passwordEditText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.buttonLogin);
        button.setOnClickListener(v ->
            onLoginClick()
        );

        loginViewModel = new LoginViewModel();

        // Osserva lo stato del login
        loginViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                // Se il login è riuscito, puoi procedere con altre azioni (e.g., navigare alla home)
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Naviga alla prossima schermata o salva l'utente (puoi usarlo per salvarlo nel database locale o in SharedPreferences)
            }
        });

        // Osserva gli errori
        loginViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Funzione che viene chiamata quando l'utente clicca il tasto di login
    public void onLoginClick() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            loginViewModel.login(email, password);
        } else {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
        }
    }
}