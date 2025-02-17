package com.novab.unisaeat.ui.view.employee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.view.user.SettingsActivity;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.Calendar;

public class HomeEmployeeActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private Button ordersButton, scanButton, settingsButton;
    private TextView nameTextView, dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_employee);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        associateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userViewModel.getUser();
    }

    private void associateUI() {
        ordersButton = findViewById(R.id.orders_btn);
        scanButton = findViewById(R.id.scan_btn);
        settingsButton = findViewById(R.id.settings_btn);


        nameTextView = findViewById(R.id.name_text);
        dateTextView = findViewById(R.id.today_date_text);

        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrdersActivity.class);

            startActivity(intent);
        });

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);

            startActivity(intent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                updateText(user);
            }
        });
        userViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                finish();
            }
        });
    }


    private void updateText(User user) {
        dateTextView.setText(Calendar.getInstance().getTime().toString());
        nameTextView.setText(String.format("%s %s", user.getName(), user.getSurname()));

    }
}
