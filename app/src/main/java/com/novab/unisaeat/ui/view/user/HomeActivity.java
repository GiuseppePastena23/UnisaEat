package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.util.Utilities;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private TextView welcomeTextView;

    private Button walletButton;
    private Button orderButton;
    private Button menuButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        associateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userViewModel.getUser();
    }


    private void associateUI() {
        welcomeTextView = findViewById(R.id.welcome_text);
        walletButton = findViewById(R.id.wallet_btn);
        orderButton = findViewById(R.id.order_btn);
        menuButton = findViewById(R.id.menu_btn);
        settingsButton = findViewById(R.id.settings_btn);

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        });

        walletButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, WalletActivity.class);
            startActivity(intent);
        });

        menuButton.setOnClickListener(v -> {
            // FIXME: This should be changed to the correct activity
            //Intent intent = new Intent(this, Utilities.class);
            //startActivity(intent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        setObservers();
    }

    private void setObservers() {
        userViewModel.getUserLiveData().observe(this, user -> {
            // FIXME: mettere black è sbagliato, bisogna mettere il colore di default
            // FIXME: cos'è il colore di default?
            welcomeTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
            welcomeTextView.setText(String.format("%s %s", getString(R.string.welcome), user.getName()));
        });
        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.e("HomeActivity", errorMessage);
                welcomeTextView.setTextColor(ContextCompat.getColor(this, R.color.red));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                    Log.e("HomeActivity", Objects.requireNonNull(e.getMessage()));
                }

                // FIXME: prova solo una volta?
                userViewModel.getUser();
            }
            /*
            Quando una richiesta fallisce non si puo solo chiudere l'app.
            anche se lo sharedpref è ha l'id facciamo lo stesso la richiesta per prednere l'utente.
            bisogna rifare la richiesta.
            è importante da tenere in mente soprattutto per il qr code perché potrebbe rimanere
            quello precedente. una soluzione semplice è renderizzarlo rosso finché errore non è null
             */


        });
    }
}