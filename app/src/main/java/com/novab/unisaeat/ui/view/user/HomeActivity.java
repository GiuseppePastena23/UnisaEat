package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

public class HomeActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private TextView userInfoView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        associateUI();
        updateLiveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLiveData();
    }

    private void updateLiveData() {
        userViewModel.getUser();
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                userInfoView.setText(user.toString());
            } else {
                userInfoView.setText("No user found");
            }
        });
    }

    private void associateUI() {
        userInfoView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, WalletActivity.class);
            startActivity(intent);
        });
    }
}