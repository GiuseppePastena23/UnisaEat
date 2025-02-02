package com.novab.unisaeat.ui.view.employee;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.Calendar;

public class HomeEmployeeActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private Button ordersButton, scanButton, exitButton;
    private TextView nameTextView, surnameTextView, emailTextView, cfTextView, dateTextView;

    // ASSOCIATE BUTTONS
    private void associateUI() {
        ordersButton = findViewById(R.id.orders_btn);
        scanButton = findViewById(R.id.scan_btn);
        exitButton = findViewById(R.id.exit_btn);

        nameTextView = findViewById(R.id.name_text);
        surnameTextView = findViewById(R.id.surname_text);
        emailTextView = findViewById(R.id.email_text);
        cfTextView = findViewById(R.id.cf_text);
        dateTextView = findViewById(R.id.today_date_text);
    }

    // set function for each button
    private void setButtonFunctions() {

        ordersButton.setOnClickListener(v -> {
            Toast.makeText(this, "Orders", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, OrdersActivity.class);
            intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
            startActivity(intent);
        });

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
            startActivity(intent);
        });

        exitButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_employee);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        updateLiveData();
        associateUI();
        setButtonFunctions();
    }

    private void updateLiveData() {

        userViewModel.getUser();
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                changeText(user);
            } else {
                Log.d("HomeEmployeeActivity", "No user found");
            }
        });
    }

    private void changeText(User user) {
        dateTextView.setText(Calendar.getInstance().getTime().toString());
        nameTextView.setText(user.getName());
        surnameTextView.setText(user.getSurname());
        emailTextView.setText(user.getEmail());
        cfTextView.setText(user.getCf());
    }
}
