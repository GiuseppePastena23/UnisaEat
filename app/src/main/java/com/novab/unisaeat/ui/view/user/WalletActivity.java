package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.ui.adapter.OrderAdapter;
import com.novab.unisaeat.ui.adapter.TransactionAdapter;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;

import java.util.List;


public class WalletActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private ListView transactionsListView;

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

    private void setAdapter(List<Transaction> transactions) {
        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        transactionsListView.setAdapter(adapter);
    }


    private void associateUI() {
        transactionsListView = findViewById(R.id.transactions_list_view);

        transactionViewModel.getTransactionsLiveData().observe(this, transactions -> {
            if (transactions != null && !transactions.isEmpty()) {
                setAdapter(transactions);
            } else {
                //TODO: set text to no transactions present
            }
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
               // TODO: show error message
            }
        });
    }
}