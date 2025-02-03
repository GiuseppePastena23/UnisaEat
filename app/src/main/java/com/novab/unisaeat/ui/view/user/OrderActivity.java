package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.adapter.ProductSpinnerAdapter;
import com.novab.unisaeat.ui.util.Utilities;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;

import java.util.HashMap;


public class OrderActivity extends AppCompatActivity {

    private Spinner productsSpinner;
    private TimePicker timePicker;
    private Button orderButton;
    private HashMap<String, Float> products;

    private void associateUI() {
        productsSpinner = findViewById(R.id.products_spinner);
        timePicker = findViewById(R.id.time_picker);
        orderButton = findViewById(R.id.order_btn);
        timePicker.setIs24HourView(true);
        populateSpinner();
    }

    private void populateSpinner() {
        products = new HashMap<>();
        products.put(getString(R.string.basket), 1.50f);
        products.put(getString(R.string.salad), 2.50f);
        products.put(getString(R.string.sandwich), 1.50f);

        ProductSpinnerAdapter spinnerAdapter = new ProductSpinnerAdapter(this, products);
        productsSpinner.setAdapter(spinnerAdapter);
    }

    private void doOrder() {
        TransactionViewModel transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        String product = productsSpinner.getSelectedItem().toString();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        float price = products.get(product) * -1; // negative value for order
        String mode = "order;" + product;

        transactionViewModel.doTransaction(price, mode);
        transactionViewModel.getTransactionOutcome().observe(this, outcome -> {
            if (outcome != null) {

                Utilities.showAlertDialog(this, getString(
                                R.string.order_confirmed),
                        getString(R.string.order_success_msg) + hour + ":" + minute + "\n" +
                                getString(R.string.products_text) + ":\n" + product);
            } else {
                Log.e("OrderActivity", "Error in order");
            }
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.d("OrderActivity", "Insufficient credit");
                Utilities.showAlertDialog(this, getString(R.string.order_denied), getString(R.string.order_credit_error_msg));
            }
        });
    }

    private boolean checkHour() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        if (hour < 9) {
            return false;
        }
        if (hour >= 22 && minute > 0) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_user);
        associateUI();
        orderButton.setOnClickListener(v -> {
            if (checkHour()) {
                doOrder();
            } else {
                Utilities.showAlertDialog(this, getString(R.string.order_denied), getString(R.string.order_time_error_msg));
            }
        });
    }


}
