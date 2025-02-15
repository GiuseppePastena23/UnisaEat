package com.novab.unisaeat.ui.view.user;

import static com.novab.unisaeat.ui.util.Utilities.creditCardClearance;
import static com.novab.unisaeat.ui.util.Utilities.showAlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;

public class RechargeCardActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private EditText ownerNameEditText, creditCardNumberEditText, expirationDateEditText, cvvEditText;
    private TextView amountTextView;
    private Button rechargeButton, increaseAmountButton, decreaseAmountButton;
    private int amount = 5; // Default amount

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_card);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        associateUI();
    }

    private void associateUI() {
        ownerNameEditText = findViewById(R.id.owner_name_edit_text);
        creditCardNumberEditText = findViewById(R.id.credit_card_number_edit_text);
        expirationDateEditText = findViewById(R.id.expiration_date_edit_text);
        cvvEditText = findViewById(R.id.cvv_edit_text);
        amountTextView = findViewById(R.id.amount_text);
        rechargeButton = findViewById(R.id.recharge_button);
        increaseAmountButton = findViewById(R.id.increase_amount_button);
        decreaseAmountButton = findViewById(R.id.decrease_amount_button);

        amountTextView.setText(String.valueOf(amount));

        increaseAmountButton.setOnClickListener(v -> {
            amount += 5;
            amountTextView.setText(String.valueOf(amount));
        });

        decreaseAmountButton.setOnClickListener(v -> {
            if (amount > 5) {
                amount -= 5;
                amountTextView.setText(String.valueOf(amount));
            }
        });

        rechargeButton.setOnClickListener(v -> processRecharge());

        transactionViewModel.getTransactionOutcome().observe(this, outcome -> {
            if (outcome != null) {
                showAlertDialog(this, "Transaction Outcome", "Transaction successful", (dialog, which) -> {
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }, false);

            }
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.e("RechargeCardActivity", errorMessage);
                showAlertDialog(this, "Error", "An error occurred: " + errorMessage);
            }
        });
    }

    private void processRecharge() {
        String ownerName = ownerNameEditText.getText().toString().trim();
        String creditCardNumber = creditCardNumberEditText.getText().toString().trim();
        String expirationDate = expirationDateEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();

        if (ownerName.isEmpty() || creditCardNumber.isEmpty() || expirationDate.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        String outcome = creditCardClearance(ownerName, creditCardNumber, expirationDate, cvv, amount);
        if (outcome.equals("OK")) {
            transactionViewModel.doTransaction(amount, "topup;online");
        } else {
            Toast.makeText(this, outcome, Toast.LENGTH_SHORT).show();
        }
    }
}
