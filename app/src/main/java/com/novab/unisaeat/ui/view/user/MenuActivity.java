package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.UtilViewModel;

public class MenuActivity extends AppCompatActivity {

    private UtilViewModel utilViewModel;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        utilViewModel = new ViewModelProvider(this).get(UtilViewModel.class);
        associateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void associateUI() {
        imageView = findViewById(R.id.imageView);
        utilViewModel.getMenu(0);
        utilViewModel.getImageBase64LiveData().observe(this, imgBase64 -> {
            if (imgBase64 != null) {
                byte[] decodedString = android.util.Base64.decode(imgBase64, android.util.Base64.DEFAULT);
                imageView.setImageBitmap(android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            }
        });
        utilViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                utilViewModel.getMenu(0);
            }
        });
    }
}
