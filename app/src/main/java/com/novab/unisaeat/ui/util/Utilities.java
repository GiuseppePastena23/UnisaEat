package com.novab.unisaeat.ui.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Utilities {

    public static void showAlertDialog(Context context, String title, String message) {
        showAlertDialog(context, title, message, null);
    }


    private static void showAlertDialog(Context context, String title, String message,
                                        DialogInterface.OnClickListener positiveAction
    ) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveAction)
                .show();
    }
}
