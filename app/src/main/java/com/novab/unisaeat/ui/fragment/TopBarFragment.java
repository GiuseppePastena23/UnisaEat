package com.novab.unisaeat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.data.model.User;
import com.novab.unisaeat.data.util.SharedPreferencesManager;
import com.novab.unisaeat.ui.view.user.RechargeWalletActivity;
import com.novab.unisaeat.ui.viewmodel.TransactionViewModel;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

public class TopBarFragment extends Fragment {

    private UserViewModel userViewModel;

    private TextView nameTextView;
    private TextView creditTextView;
    private Button addCreditButton;

    public TopBarFragment() {
        // Required empty public constructor
    }

    private void associateUIElements(View view) {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getContext());

        User user = sharedPreferencesManager.getUser();

        nameTextView = view.findViewById(R.id.name_text_view);
        creditTextView = view.findViewById(R.id.credit_text_view);
        addCreditButton = view.findViewById(R.id.addCredit_btn);

        nameTextView.setText(String.format("%s %s", user.getName(), user.getSurname()));
        creditTextView.setText(String.format("%.2f€", user.getCredit()));
        addCreditButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RechargeWalletActivity.class);
            startActivity(intent);

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topbar_fragment, container, false);

        associateUIElements(view);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        updateCredit();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCredit();
    }

    public void updateCredit() {
        userViewModel.getUser();
        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                nameTextView.setText(String.format("%s %s", user.getName(), user.getSurname()));

                // Format the credit to show 2 decimal places and the Euro symbol
                creditTextView.setText(String.format("%.2f€", user.getCredit()));
            }
        });
    }
}
