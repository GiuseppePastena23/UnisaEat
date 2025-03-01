package com.novab.unisaeat.ui.view.user;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.util.SharedPreferencesManager;
import com.novab.unisaeat.ui.fragment.BottomBarFragment;
import com.novab.unisaeat.ui.fragment.QrCodeFragment;
import com.novab.unisaeat.ui.fragment.TopBarFragment;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

public class HomeActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_CODE = 1;
    SharedPreferencesManager sharedPreferencesManager;

    private UserViewModel userViewModel;
    private TransactionViewModel transactionViewModel;

    private final Handler handler = new Handler(Looper.getMainLooper());

    TopBarFragment topBarFragment;


    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            transactionViewModel.getUserTransactions();
            transactionViewModel.getUpdateCreditLiveData().observe(HomeActivity.this, update -> {
                if (update) {
                    recreate();
                }
            });
            handler.postDelayed(this, 2000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("HomeActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        checkAndRequestNotificationPermission();

        topBarFragment = new TopBarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.top_fragment_container, topBarFragment)
                .commit();

        // Load Bottom Bar Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_fragment_container, new BottomBarFragment())
                .commit();

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

        handler.postDelayed(runnable, 5000);
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
                .replace(R.id.qr_fragment_container, new QrCodeFragment())
                .commit();
    }


    private void associateUI() {
        setObservers();
    }


    private void setObservers() {
        userViewModel.getUserLiveData().observe(this, user -> {

        });
        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
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

    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }
}