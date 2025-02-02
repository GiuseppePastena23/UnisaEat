package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            finish();
        }

        //associateButtons();
        //setButtonFunctions();


    }
}
