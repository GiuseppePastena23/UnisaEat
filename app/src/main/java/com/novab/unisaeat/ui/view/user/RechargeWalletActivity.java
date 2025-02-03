package com.novab.unisaeat.ui.view.user;

import static com.novab.unisaeat.ui.util.Utilities.creditCardClearance;
import static com.novab.unisaeat.ui.util.Utilities.showAlertDialog;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;


public class RechargeWalletActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;

    private String transactionOutcome;
    private EditText ownerNameEditText, creditCardNumberEditText, expirationDateEditText, cvvEditText, amountEditText;
    private Button rechargeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_wallet);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        associateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void associateUI() {
        ownerNameEditText = findViewById(R.id.owner_name_edit_text);
        creditCardNumberEditText = findViewById(R.id.credit_card_number_edit_text);
        expirationDateEditText = findViewById(R.id.expiration_date_edit_text);
        cvvEditText = findViewById(R.id.cvv_edit_text);
        amountEditText = findViewById(R.id.amount_edit_text);
        rechargeButton = findViewById(R.id.recharge_button);
        rechargeButton.setOnClickListener(v -> {
            String ownerName = ownerNameEditText.getText().toString();
            String creditCardNumber = creditCardNumberEditText.getText().toString();
            String expirationDate = expirationDateEditText.getText().toString();
            String cvv = cvvEditText.getText().toString();
            String amountString = amountEditText.getText().toString();
            if (ownerName.isEmpty() || creditCardNumber.isEmpty() || expirationDate.isEmpty() || cvv.isEmpty() || amountString.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            float amount = Float.parseFloat(amountString);
            String outcome = creditCardClearance(ownerName, creditCardNumber, expirationDate, cvv, amount);
            if (outcome.equals("OK")) {
                transactionViewModel.doTransaction(amount, "topup;online");
            } else {
                Toast.makeText(this, outcome, Toast.LENGTH_SHORT).show();
            }
        });

        transactionViewModel.getTransactionOutcome().observe(this, outcome -> {
            if (outcome != null) {
                transactionOutcome = outcome;
                showAlertDialog(this, "Transaction Outcome", transactionOutcome);
            }
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                showAlertDialog(this, "Error", errorMessage);
            }
        });
    }


}