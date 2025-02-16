package com.novab.unisaeat.ui.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.Transaction;
import com.novab.unisaeat.ui.adapter.TransactionAdapter;
import com.novab.unisaeat.ui.fragment.TopBarFragment;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

import java.util.List;

public class WalletActivity extends AppCompatActivity {

    private TransactionViewModel transactionViewModel;
    private UserViewModel userViewModel;

    private ListView transactionsListView;
    private ProgressBar progressBar;
    private TextView noTransactionFoundTextView;
    private Button rechargeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_user);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        associateUI();
        loadTopBarFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        transactionViewModel.getUserTransactions();
        userViewModel.getUser();
    }

    private void setAdapter(List<Transaction> transactions) {
        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        transactionsListView.setAdapter(adapter);
    }

    private void associateUI() {
        progressBar = findViewById(R.id.progress_bar);
        noTransactionFoundTextView = findViewById(R.id.no_transaction_found_text_view);
        transactionsListView = findViewById(R.id.transactions_list_view);
        rechargeButton = findViewById(R.id.goto_recharge_btn);
        rechargeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RechargeWalletActivity.class);
            startActivity(intent);
        });
        observeViewModel();
    }

    private void observeViewModel() {
        transactionViewModel.getTransactionsLiveData().observe(this, transactions -> {
            if (transactions != null && !transactions.isEmpty()) {
                setAdapter(transactions);
            } else {
                progressBar.setVisibility(View.GONE);
                transactionsListView.setVisibility(View.GONE);
                noTransactionFoundTextView.setVisibility(View.VISIBLE);
            }
        });

        transactionViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Log.e("WalletActivity", errorMessage);
                transactionViewModel.getUserTransactions();
            }
        });

        transactionViewModel.getIsLoadingLiveData().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadTopBarFragment() {
        Fragment topBarFragment = new TopBarFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.top_fragment_container, topBarFragment)
                .commit();
    }
}
