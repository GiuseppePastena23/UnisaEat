package com.novab.unisaeat.ui.util;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilities {

    public static void showAlertDialog(Context context, String title, String message) {
        showAlertDialog(context, title, message, null);
    }


    public static void showAlertDialog(Context context, String title, String message,
                                        DialogInterface.OnClickListener positiveAction
    ) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveAction)
                .show();
    }

    public static void showAlertDialog(Context context, String title, String message,
                                       DialogInterface.OnClickListener positiveAction, boolean cancelable
    ) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", positiveAction)
                .show()
                .setCancelable(cancelable);

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




    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }

                return hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("SHA-256 algorithm not found", e);
            }
        }


    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }


    public static boolean isServerReachable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("172.26.117.132", 5000), 3000); // Timeout set to 3 seconds
            return true;
        } catch (UnknownHostException e) {
            Log.d("Server", "Host sconosciuto: strawberry", e);
            return false;
        } catch (IOException e) {
            Log.d("Server", "Errore di connessione: " + e.getMessage(), e);
            return false;
        }

    }



}
