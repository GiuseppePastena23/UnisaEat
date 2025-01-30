package com.novab.unisaeat.ui.view.employee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.view.SettingsActivity;

public class HomeEmployeeActivity extends AppCompatActivity {

    private Button ordersButton, scanButton, settingsButton;

    // ASSOCIATE BUTTONS
    private void associateButtons() {
        ordersButton = findViewById(R.id.orders_btn);
        scanButton = findViewById(R.id.scan_btn);
        settingsButton = findViewById(R.id.settings_btn);
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

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("user", (User) getIntent().getSerializableExtra("user"));
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_employee);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            finish();
        }


        associateButtons();
        setButtonFunctions();


    }
}
