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

    /*
     * should return OK if everything is fine
     * should return an error message if something is wrong
     * @param ownerName
     * @param creditCardNumber
     * @param expirationDate
     * @param cvv
     * @param amount
     * @return
     */
    public static String creditCardClearance(String ownerName, String creditCardNumber, String expirationDate, String cvv, float amount) {
        String message = "OK";
        if (ownerName.length() < 3) {
            message = "Owner name is too short";
        } else if (creditCardNumber.length() != 16) {
            message = "Credit card number should be 16 digits";
        } else if (expirationDate.length() != 5) {
            message = "Expiration date should be in the format MM/YY";
        } else if (cvv.length() != 3) {
            message = "CVV should be 3 digits";
        } else if (amount <= 0) {
            message = "Amount should be positive";
        }
        return message;
    }
}
