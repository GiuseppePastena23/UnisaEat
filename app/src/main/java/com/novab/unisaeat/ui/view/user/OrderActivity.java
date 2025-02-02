package com.novab.unisaeat.ui.view.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;

import java.util.ArrayList;
import java.util.Map;


public class OrderActivity extends AppCompatActivity {

    private Spinner productsSpinner;
    private TimePicker timePicker;
    private Button orderButton;

    private Map<String, Float> products;


    private void associateUI() {
        productsSpinner = findViewById(R.id.products_spinner);
        timePicker = findViewById(R.id.time_picker);
        orderButton = findViewById(R.id.order_btn);
    }


    private void populateSpinner() {
        products.put(getString(R.string.basket), 1.50f);
        products.put(getString(R.string.salad), 2.50f);
        products.put(getString(R.string.sandwich), 1.50f);


        ArrayList<String> productsList = new ArrayList<>(products.keySet());
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productsList);
        productsSpinner.setAdapter(spinnerAdapter);
    }

    private void order(int userId) {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        String product = productsSpinner.getSelectedItem().toString();
        float price = products.get(product);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String mode = "order;" + product;

        transactionViewModel.doTransaction(userId, price, mode);

        String message = "Hai ordinato: " + product + " alle " + hour + ":" + minute;
        showAlertDialog("Prenotazione Confermata", message);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_user);

        orderButton.setOnClickListener(v -> {

        });


    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (title.equals("Prenotazione Confermata")) {

                        }
                    }
                })
                .create()
                .show();
    }
}
