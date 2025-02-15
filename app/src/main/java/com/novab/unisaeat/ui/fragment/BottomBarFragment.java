package com.novab.unisaeat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.view.user.MenuActivity;
import com.novab.unisaeat.ui.view.user.OrderActivity;
import com.novab.unisaeat.ui.view.user.SettingsActivity;
import com.novab.unisaeat.ui.view.user.WalletActivity;

public class BottomBarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottombar_fragment, container, false);

        ImageButton walletButton = view.findViewById(R.id.wallet_btn);
        ImageButton orderButton = view.findViewById(R.id.order_btn);
        ImageButton menuButton = view.findViewById(R.id.menu_btn);
        ImageButton settingsButton = view.findViewById(R.id.settings_btn);

        walletButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WalletActivity.class);
            startActivity(intent);
        });

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            startActivity(intent);
        });

        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MenuActivity.class);
            startActivity(intent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}