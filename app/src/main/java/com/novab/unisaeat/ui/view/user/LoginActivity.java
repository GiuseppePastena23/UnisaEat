package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.view.employee.HomeEmployeeActivity;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private EditText emailEditText, passwordEditText;
    private Button button;
    private TextView userInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        userInfoView = findViewById(R.id.userInfo);
        button = findViewById(R.id.buttonLogin);
        button.setOnClickListener(v ->
                onLoginClick()
        );


        // Osserva i cambiamenti del LiveData dell'utente
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, user.getStatus().equals("employee") ? HomeEmployeeActivity.class : HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                userInfoView.setText("Nessun utente trovato");
            }
        });

        // Osserva gli errori
        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                userInfoView.setText("Errore: " + errorMessage);
            }
        });

    }

    // Funzione che viene chiamata quando l'utente clicca il tasto di login
    public void onLoginClick() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // DEBUG:
        // email = "m.r";
        password = "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb";

        if (!email.isEmpty() && !password.isEmpty()) {
            userViewModel.login(email, password);

        } else {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
        }
    }
}