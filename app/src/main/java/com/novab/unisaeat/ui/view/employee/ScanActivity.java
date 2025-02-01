package com.novab.unisaeat.ui.view.employee;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.viewmodel.ScanViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanActivity extends AppCompatActivity {

   private ScanViewModel scanViewModel;


    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    scanViewModel.setScanResult(result.getContents());
                } else {
                    scanViewModel.setScanResult(null);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);
        User user = (User) getIntent().getSerializableExtra("user");
        if (user == null) {
            finish();
        }

        scanViewModel.startQrCodeScanner(barcodeLauncher, this.getApplicationContext());


        scanViewModel.getScanResult().observe(this, result -> {
            if (result != null) {
                Toast.makeText(this, "Scanned: " + result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, PaymentActivity.class);
                String id = extractID(result);
                String cf = extractCf(result);
                String token = extractToken(result);
                intent.putExtra("user_id", id); // passa l'utente che deve pagare il pasto
                intent.putExtra("cf", cf); // passa il codice fiscale dell'utente che deve pagare il pasto
                intent.putExtra("token", token); // passa il token dell'utente che deve pagare il pasto
                startActivity(intent);

            } else {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
            }
            finish();
        });


    }

    // Estrae l'ID dall'input
    public static String extractID(String str) {
    Pattern pattern = Pattern.compile("^(\\d+)\\|([A-Z]+)\\|([A-Z0-9]+)$");
    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
        return matcher.group(1);
    }
    return null;
}

// Estrae il codice fiscale dall'input
public static String extractCf(String str) {
    Pattern pattern = Pattern.compile("^(\\d+)\\|([A-Z]+)\\|([A-Z0-9]+)$");
    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
        return matcher.group(2);
    }
    return null;
}

public static String extractToken(String str) {
    Pattern pattern = Pattern.compile("^(\\d+)\\|([A-Z]+)\\|([A-Z0-9]+)$");
    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
        return matcher.group(3);
    }
    return null;
}



}
