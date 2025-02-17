package com.novab.unisaeat.ui.view.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class RechargePaypalActivity extends AppCompatActivity {

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK) // Change to ENVIRONMENT_SANDBOX for testing
            .clientId("Ad4d3g_EehvMvNMeaMvVaZXkLoNS5zlHoG6QIjNiW0Xg0CjnRmBxj8PVVHjc9hIYZqYAlpz5QgdaEc3b");
    private TransactionViewModel transactionViewModel;
    private TextView amountTextView;
    private Button rechargeButton, increaseAmountButton, decreaseAmountButton;
    private int amount = 5; // Default amount

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_paypal);

        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Initialize UI elements
        associateUI();
    }

    private void associateUI() {
        amountTextView = findViewById(R.id.amount_text);  // TextView to display amount
        rechargeButton = findViewById(R.id.recharge_button);
        increaseAmountButton = findViewById(R.id.increase_amount_button);
        decreaseAmountButton = findViewById(R.id.decrease_amount_button);

        amountTextView.setText(String.valueOf(amount));

        increaseAmountButton.setOnClickListener(v -> {
            amount += 5;  // Increase by 5 each time
            amountTextView.setText(String.valueOf(amount));
        });

        decreaseAmountButton.setOnClickListener(v -> {
            if (amount > 5) {  // Prevent amount from going below 5
                amount -= 5;
                amountTextView.setText(String.valueOf(amount));
            }
        });

        rechargeButton.setOnClickListener(v -> processPayPalPayment());
    }

    private void processPayPalPayment() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", "Recharge Wallet", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(RechargePaypalActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("PaymentConfirmation", paymentDetails);
                        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                        transactionViewModel.doTransaction(amount, "topup;online");
                        finish();
                    } catch (JSONException e) {
                        Log.e("PaymentConfirmation", "Error occurred during payment confirmation", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
