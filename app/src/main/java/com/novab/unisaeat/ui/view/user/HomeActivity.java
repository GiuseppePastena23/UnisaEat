package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;

public class HomeActivity extends AppCompatActivity {

    private TextView userInfoView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        associateUI();

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            finish();
        }

        userInfoView.setText(String.format("Benvenuto %s (%s) %s", user.getStatus(), user.getEmail(), user.getToken()));


    }

    private void associateUI() {
        userInfoView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            Log.d("TAG", "associateUI: button clicked");
            Intent intent = new Intent(this, WalletActivity.class);
            intent.putExtra("user", getIntent().getSerializableExtra("user"));
            startActivity(intent);
        });
    }
}