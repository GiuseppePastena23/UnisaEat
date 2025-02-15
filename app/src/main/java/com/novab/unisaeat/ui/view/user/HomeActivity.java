package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.util.SharedPreferencesManager;
import com.novab.unisaeat.ui.fragment.QrCodeFragment;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    SharedPreferencesManager sharedPreferencesManager;

    private UserViewModel userViewModel;
    private TransactionViewModel transactionViewModel;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private TextView welcomeTextView;

    private Button walletButton;
    private Button orderButton;
    private Button menuButton;
    private Button settingsButton;


    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            transactionViewModel.getUserTransactions();
            handler.postDelayed(this, 2000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        associateUI();
        showQRCode();
    }


    @Override
    protected void onResume() {
        super.onResume();
        userViewModel.getUser();
        showQRCode();

        handler.postDelayed(runnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public void showQRCode() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new QrCodeFragment())
                .commit();
    }


    private void associateUI() {
        welcomeTextView = findViewById(R.id.welcome_text);
        welcomeTextView.setText(String.format("%s %s", getString(R.string.welcome), sharedPreferencesManager.getUser().getName()));

        walletButton = findViewById(R.id.goto_recharge_btn);

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
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });



        setObservers();
    }


    private void setObservers() {
        userViewModel.getUserLiveData().observe(this, user -> {
            welcomeTextView.setText(String.format("%s %s %s", getString(R.string.welcome), user.getName(), user.getToken()));
        });
        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.e("HomeActivity", errorMessage);
                userViewModel.getUser();
            }
            /*
            Quando una richiesta fallisce non si puo solo chiudere l'app.
            anche se lo sharedpref e ha l'id facciamo lo stesso la richiesta per prednere l'utente.
            bisogna rifare la richiesta.
            è importante da tenere in mente soprattutto per il qr code perché potrebbe rimanere
            quello precedente. una soluzione semplice è renderizzarlo rosso finché errore non è null
             */
        });
    }
}