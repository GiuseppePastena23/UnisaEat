package com.novab.unisaeat.ui.view;

import static com.novab.unisaeat.ui.util.NotificationHelper.shouldSendNotification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.util.NotificationHelper;
import com.novab.unisaeat.ui.util.NotificationWorker;
import com.novab.unisaeat.ui.view.user.LoginActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationHelper.createNotificationChannel(this);
        scheduleNotifications();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void scheduleNotifications() {
        Log.d("TAG", "scheduleNotifications: Scheduling notifications...");

        scheduleRecurringNotification("PROMO", 60 * 24);

        if (shouldSendNotification(this, "LOW_CREDIT")) {
            scheduleOneTimeNotification("LOW_CREDIT", 0);
        }
    }

    private void scheduleOneTimeNotification(String type, long delay) {
        scheduleNotification(type, delay, false);
    }

    private void scheduleRecurringNotification(String type, long interval) {
        scheduleNotification(type, interval, true);
    }

    private void scheduleNotification(String type, long time, boolean isRecurring) {
        Data data = new Data.Builder()
                .putString(NotificationWorker.NOTIFICATION_TYPE, type)
                .build();

        if (isRecurring) {
            PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(NotificationWorker.class, time, TimeUnit.MINUTES)
                    .setInputData(data)
                    .build();
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(type, ExistingPeriodicWorkPolicy.KEEP, request);
        } else {
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                    .setInitialDelay(time, TimeUnit.HOURS)
                    .setInputData(data)
                    .build();
            WorkManager.getInstance(this).enqueue(request);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scheduleNotifications();
        }
    }
}
