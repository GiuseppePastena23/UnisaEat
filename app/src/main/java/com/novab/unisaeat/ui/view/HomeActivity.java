package com.novab.unisaeat.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.viewmodel.LoginViewModel;

public class HomeActivity extends AppCompatActivity {

    private TextView userInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userInfoView = findViewById(R.id.textView);
        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            finish();
        }

        userInfoView.setText(String.format("Benvenuto %s (%s) %s", user.getStatus(), user.getEmail(), user.getToken()));


    }
}