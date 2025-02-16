package com.novab.unisaeat.ui.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.os.Build;

public class NotificationHelper {
    private static final String CHANNEL_ID = "general_notifications";
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final long NOTIFICATION_INTERVAL = 24 * 60 * 60 * 1000; // 24 ore

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID, "Notifiche Generali", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Canale per le notifiche generali di UnisaEat");
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static boolean shouldSendNotification(Context context, String notificationType) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastTime = prefs.getLong(notificationType, 0);
        // return System.currentTimeMillis() - lastTime > NOTIFICATION_INTERVAL;
        return true;
    }

    public static void updateLastNotificationTime(Context context, String notificationType) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(notificationType, System.currentTimeMillis());
        editor.apply();
    }

    public static void showNotification(Context context, String title, String message) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
