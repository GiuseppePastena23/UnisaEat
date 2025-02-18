package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.fragment.TopBarFragment;
import com.paypal.android.sdk.payments.PayPalPayment;

import java.math.BigDecimal;


public class RechargeWalletActivity extends AppCompatActivity {

    private Button btnPayPal, btnGPay, btnCreditCard;
    private TopBarFragment topBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_wallet);

        btnPayPal = findViewById(R.id.btnPayPal);
        btnGPay = findViewById(R.id.btnGPay);
        btnCreditCard = findViewById(R.id.btnCreditCard);

        associateUi();

        topBarFragment = new TopBarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.top_fragment_container, topBarFragment)
                .commit();
    }


    private void associateUi() {
        // PayPal Button click listener
        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start PayPal payment flow
                PayPalPayment payment = new PayPalPayment(new BigDecimal("10.00"), "USD", "Recharge Wallet", PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(RechargeWalletActivity.this, RechargePaypalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnGPay.setOnClickListener(v -> Toast.makeText(getApplicationContext(), getString(R.string.gpay_not_implemented), Toast.LENGTH_SHORT).show());

        btnCreditCard.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RechargeCardActivity.class);
            startActivity(intent);
        });
    }
}
