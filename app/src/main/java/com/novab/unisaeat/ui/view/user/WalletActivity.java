package com.novab.unisaeat.ui.view.user;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.ui.viewmodel.WalletViewModel;

public class WalletActivity extends AppCompatActivity {

    private WalletViewModel walletViewModel;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        walletViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        associateUI();
        User user = (User) getIntent().getSerializableExtra("user");

        assert user != null;
        walletViewModel.getUserTransaction(user.getId());

        walletViewModel.getTransactionsLiveData().observe(this, transactions -> {
            if (transactions != null) {
                textView.setText(transactions.toString());
            }
        });

        walletViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                textView.setText(errorMessage);
            }
        });


    }

    private void associateUI() {
        textView = findViewById(R.id.textView);
    }
}