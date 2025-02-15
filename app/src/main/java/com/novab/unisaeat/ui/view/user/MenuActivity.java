package com.novab.unisaeat.ui.view.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.UtilViewModel;

import java.time.LocalDate;

public class MenuActivity extends AppCompatActivity {

    private UtilViewModel utilViewModel;

    private Button[] buttons = new Button[5];
    private ImageView imageView;
    private ProgressBar progressBar;

    private int selectedDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDay = LocalDate.now().getDayOfWeek().getValue() - 1;
        }
        if (selectedDay > 4) {
            selectedDay = 0;
        }

        utilViewModel = new ViewModelProvider(this).get(UtilViewModel.class);
        associateUI();
        utilViewModel.getDayMenu(selectedDay);
        setLiveDataObservers();
    }

    private void associateUI() {
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);

        buttons[0] = findViewById(R.id.monday_btn);
        buttons[1] = findViewById(R.id.tuesday_btn);
        buttons[2] = findViewById(R.id.wednesday_btn);
        buttons[3] = findViewById(R.id.thursday_btn);
        buttons[4] = findViewById(R.id.friday_btn);
        updateButtonColors();

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            buttons[i].setOnClickListener(v -> {
                selectedDay = finalI;
                utilViewModel.getDayMenu(finalI);
                updateButtonColors();
            });
        }
    }

    private void updateButtonColors() {
        for (int i = 0; i < 5; i++) {
            buttons[i].setBackgroundColor(i == selectedDay ? getResources().getColor(R.color.primary, null) : getResources().getColor(R.color.fourth, null));
        }
    }

    private void setLiveDataObservers() {
        utilViewModel.getImagesBase64LiveData().observe(this, imgBase64 -> {
            if (imgBase64 != null && !imgBase64.isEmpty()) {
                try {
                    byte[] decodedBytes = Base64.decode(imgBase64, Base64.DEFAULT);
                    if (decodedBytes.length > 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(this, "Errore: immagine vuota", Toast.LENGTH_SHORT).show();
                    }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(this, "Errore: stringa Base64 non valida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Errore: immagine non disponibile", Toast.LENGTH_SHORT).show();
            }
        });


        utilViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        utilViewModel.getIsLoadingLiveData().observe(this, isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? ProgressBar.VISIBLE : ProgressBar.GONE);
            }
        });

    }

}
