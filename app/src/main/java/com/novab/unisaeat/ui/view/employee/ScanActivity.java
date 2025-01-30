package com.novab.unisaeat.ui.view.employee;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            finish();
        }

        //associateButtons();
        //setButtonFunctions();


    }
}
