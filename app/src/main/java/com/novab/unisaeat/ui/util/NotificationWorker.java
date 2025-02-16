package com.novab.unisaeat.ui.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.util.SharedPreferencesManager;

public class NotificationWorker extends Worker {

    public static final String NOTIFICATION_TYPE = "notification_type";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        String notificationType = getInputData().getString(NOTIFICATION_TYPE);

        if (notificationType != null) {
            switch (notificationType) {
                case "LOW_CREDIT":
                    checkAndNotifyLowCredit(context);
                    break;
                case "PROMO":
                    sendNotification(context,
                            getApplicationContext().getString(R.string.promo_notification_title),
                            getApplicationContext().getString(R.string.promo_notification_message)
                    );
                    break;
                case "ORDER":
                    sendNotification(context,
                            getApplicationContext().getString(R.string.order_notification_title),
                            getApplicationContext().getString(R.string.order_notification_message)
                    );
            }
        }

        return Result.success();
    }

    private void checkAndNotifyLowCredit(Context context) {
        float credito = getUserCredit();
        if (credito != -1f && credito < 5f && NotificationHelper.shouldSendNotification(context, "LOW_CREDIT")) {
            sendNotification(context, "Low Credit", "Il tuo credito è inferiore a 5€! Ricarica ora.");
            NotificationHelper.updateLastNotificationTime(context, "LOW_CREDIT");
        }
    }

    private float getUserCredit() {
        SharedPreferencesManager preferences = new SharedPreferencesManager(getApplicationContext());
        return preferences.getUserCredit();
    }

    private void sendNotification(Context context, String title, String message) {
        NotificationHelper.showNotification(context, title, message);
    }
}
