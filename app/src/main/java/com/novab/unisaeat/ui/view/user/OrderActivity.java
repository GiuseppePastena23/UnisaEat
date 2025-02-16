package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.adapter.ProductSpinnerAdapter;
import com.novab.unisaeat.ui.fragment.TopBarFragment;
import com.novab.unisaeat.ui.util.Utilities;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;

import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {

    private Spinner productsSpinner;
    private TimePicker timePicker;
    private Button orderButton;
    private TextView totalValue;
    private TransactionViewModel transactionViewModel;
    private HashMap<String, Float> products;

    private void associateUI() {
        productsSpinner = findViewById(R.id.products_spinner);
        timePicker = findViewById(R.id.time_picker);
        orderButton = findViewById(R.id.order_btn);
        totalValue = findViewById(R.id.total_value);
        timePicker.setIs24HourView(true);
        populateSpinner();
        updateTotalValue();
    }

    private void populateSpinner() {
        products = new HashMap<>();
        products.put(getString(R.string.basket), 3.5f);
        products.put(getString(R.string.salad), 2.5f);
        products.put(getString(R.string.sandwich), 3f);

        ProductSpinnerAdapter spinnerAdapter = new ProductSpinnerAdapter(this, products);
        productsSpinner.setAdapter(spinnerAdapter);
        productsSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                updateTotalValue();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void updateTotalValue() {
        String product = productsSpinner.getSelectedItem().toString();
        float price = products.get(product);
        totalValue.setText("€ " + String.format("%.2f", price));
    }

    private void doOrder() {
        String product = productsSpinner.getSelectedItem().toString();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        float price = products.get(product) * -1; // negative value for order
        String mode = "order;" + product;

        transactionViewModel.doTransaction(price, mode);
        transactionViewModel.getTransactionOutcome().observe(this, outcome -> {
            if (outcome != null) {
                Utilities.showAlertDialog(this, getString(R.string.order_confirmed),
                        getString(R.string.order_success_msg) + hour + ":" + minute + "\n" +
                                getString(R.string.products_text) + ":\n" + product, (dialog, which) -> {
                            finish();
                        });
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
        // Get the current hour and minute
        transactionViewModel.getDay();
        int currentHour = transactionViewModel.getDayLiveData().getValue().getHour();
        int currentMinute = transactionViewModel.getDayLiveData().getValue().getMinute();

        // Get the selected hour and minute
        int selectedHour = timePicker.getHour();
        int selectedMinute = timePicker.getMinute();

        // If the selected time is before 09:00, it's not valid
        if (selectedHour < 9) {
            return false;
        }

        // If the selected time is after 22:00, it's not valid
        if (selectedHour > 22 || (selectedHour == 22 && selectedMinute > 0)) {
            return false;
        }

        // If the selected time is before the current time, it's not valid
        if (selectedHour < currentHour || (selectedHour == currentHour && selectedMinute < currentMinute)) {
            return false;
        }

        return true; // If all conditions are met, return true
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        checkDay();

    }

    private void checkDay() {
        transactionViewModel.getDay();
        transactionViewModel.getDayLiveData().observe(this, dayInfo -> {
            int weekDay = dayInfo.getDay();
            int hour = dayInfo.getHour();
            int minute = dayInfo.getMinute();
            weekDay = 1; // TODO: remove
            // CHECK IF DAY IS SATURDAY OR SUNDAY 5=Saturday 6=Sunday
            if (weekDay == 5 || weekDay == 6) {
                Utilities.showAlertDialog(this, getString(R.string.order_denied), getString(R.string.order_day_error_msg), (dialog, which) -> {
                    finish();
                }, false);
            } else {
                // CHECK IF TIME IS AFTER 22:00
                if (hour > 22 || (hour == 22 && minute > 0)) {
                    Utilities.showAlertDialog(this, getString(R.string.order_denied), getString(R.string.order_hour_error_msg), (dialog, which) -> {
                        finish();
                    }, false);
                }
                // EVERYTHING GOOD
                setContentView(R.layout.activity_order_user);
                TopBarFragment topBarFragment = new TopBarFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, topBarFragment)
                        .commit();
                associateUI();
                orderButton.setOnClickListener(v -> {
                    if (checkHour()) {
                        doOrder();
                    } else {
                        Utilities.showAlertDialog(this, getString(R.string.order_denied), getString(R.string.order_time_error_msg));
                    }
                });
            }
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.e("OrderActivity", errorMessage);
                transactionViewModel.getDay();
            }
        });
    }
}
