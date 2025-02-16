package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.util.SharedPreferencesManager;

public class UserInfoActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView cfTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private TextView statusTextView;
    private TextView creditInfoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        User user = sharedPreferencesManager.getUser();

        associateUI();
        if (user != null) {
            setData(user);
        } else {
            finish();
        }
    }

    private void setData(User user) {
        String fullName = user.getName() + " " + user.getSurname();
        int statusResId;

        switch (user.getStatus()) {
            case "student":
                statusResId = R.string.student;
                break;
            case "professor":
                statusResId = R.string.professor;
                break;
            case "employee":
                statusResId = R.string.employee;
                break;
            default:
                statusResId = R.string.student;
                break;
        }

        fullNameTextView.setText(fullName);
        cfTextView.setText(user.getCf());
        emailTextView.setText(user.getEmail());
        phoneNumberTextView.setText(user.getPhone());
        statusTextView.setText(statusResId);
        creditInfoTextView.setText(String.valueOf(user.getCredit()));
    }


    private void associateUI() {
        fullNameTextView = findViewById(R.id.full_name_text_view);
        cfTextView = findViewById(R.id.cf_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        phoneNumberTextView = findViewById(R.id.phone_number_text_view);
        statusTextView = findViewById(R.id.status_text_view);
        creditInfoTextView = findViewById(R.id.credit_info_text_view);
    }
}
