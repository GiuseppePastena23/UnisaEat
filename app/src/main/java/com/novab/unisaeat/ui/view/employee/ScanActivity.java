package com.novab.unisaeat.ui.view.employee;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.novab.unisaeat.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanActivity extends AppCompatActivity {

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    handleScanResult(result.getContents());
                } else {
                    handleScanResult(null);
                }
            });

    // Extract the ID from the QR code string
    // Extract the ID from the QR code string
    // Extract the ID from the QR code string
    public static String extractID(String str) {
        // Pattern: Matches the ID before the first ':'
        Pattern pattern = Pattern.compile("^([^:]+):");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1); // ID
        }
        return null;
    }

    // Extract the fiscal code (cf) from the QR code string
    public static String extractCf(String str) {
        // Pattern: Matches the cf between the first ':' and the second ':'
        Pattern pattern = Pattern.compile("^\\d+:(.*?):");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1); // Fiscal code
        }
        return null;
    }

    // Extract the token from the QR code string
    public static String extractToken(String str) {
        // Pattern: Matches the token after the second ':', but no colon after the token
        Pattern pattern = Pattern.compile("^\\d+:.*?:(.*)$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1); // Token
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_employee);


        startQrCodeScanner();
    }

    private void startQrCodeScanner() {
        // Create ScanOptions
        ScanOptions options = new ScanOptions();
        options.setPrompt(getString(R.string.scan_prompt));
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CaptureActivity.class);

        // Launch the QR code scanner
        barcodeLauncher.launch(options);
    }

    private void handleScanResult(String result) {
        if (result != null) {
            // If QR code is scanned, show a message and pass data to PaymentActivity
            //Toast.makeText(this, "Scanned: " + result, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, PaymentActivity.class);

            String id = extractID(result);
            String cf = extractCf(result);
            String token = extractToken(result);

            intent.putExtra("user_id", id); // pass the user ID for the payment
            intent.putExtra("cf", cf); // pass the user fiscal code
            intent.putExtra("token", token); // pass the token
            startActivity(intent);
        } else {
            // If scan is canceled, show a message
            //Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_SHORT).show();
        }

        finish(); // Close the activity after handling the result
    }


}
