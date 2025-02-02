package com.novab.unisaeat.ui.view.employee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.view.user.SettingsActivity;

public class HomeEmployeeActivity extends AppCompatActivity {

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

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            finish();
        }
        else {
            associateUI();
            setButtonFunctions();
            changeText(user);
        }



    }

    private void changeText(User user) {
        dateTextView.setText(Calendar.getInstance().getTime().toString());
        nameTextView.setText(user.getName());
        surnameTextView.setText(user.getSurname());
        emailTextView.setText(user.getEmail());
        cfTextView.setText(user.getCf());
    }
}
