package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;


public class WalletActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_user);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        associateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        transactionViewModel.getUserTransaction();
    }


    private void associateUI() {
        textView = findViewById(R.id.textView);

        transactionViewModel.getTransactionsLiveData().observe(this, transactions -> {
            if (transactions != null) {
                textView.setText(transactions.toString());
            }
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                textView.setText(errorMessage);
            }
        });
    }
}