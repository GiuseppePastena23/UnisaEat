package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.util.SharedPreferencesManager;

public class UserInfoActivity extends AppCompatActivity {

    private TextView userNameText;
    private TextView userSurnameText;
    private TextView userCfText;
    private TextView userEmailText;
    private TextView userStatusText;
    private TextView userPhoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);

        associateUI();

        User user = sharedPreferencesManager.getUser();

        if (user != null) {
            setData(user);
        }
    }

    private void setData(User user) {
        userNameText.setText(user.getName());
        userSurnameText.setText(user.getSurname());
        userCfText.setText(user.getCf());
        userEmailText.setText(user.getEmail());
        userStatusText.setText(user.getStatus());
        userPhoneText.setText(user.getPhone());
    }

    private void associateUI() {
        userNameText = findViewById(R.id.user_name_text);
        userSurnameText = findViewById(R.id.user_surname_text);
        userCfText = findViewById(R.id.user_cf_text);
        userEmailText = findViewById(R.id.user_email_text);
        userStatusText = findViewById(R.id.user_status_text);
        userPhoneText = findViewById(R.id.user_phone_text);
    }
}
