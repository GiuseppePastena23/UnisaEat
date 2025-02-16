package com.novab.unisaeat.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;
import com.novab.unisaeat.ui.viewmodel.UtilViewModel;

public class QrCodeFragment extends Fragment {

    private UserViewModel userViewModel;
    private UtilViewModel utilViewModel;
    private ImageView qrImageView;
    private ProgressBar progressBar;

    public QrCodeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);
        qrImageView = rootView.findViewById(R.id.qr_image_view);
        progressBar = rootView.findViewById(R.id.progress_bar);

        // Initialize ViewModels
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        utilViewModel = new ViewModelProvider(this).get(UtilViewModel.class);

        // Observe the user LiveData to trigger QR code generation
        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                String qrCode = user.getId() + ":" + user.getCf() + ":" + user.getToken();

                // Show progress bar while the QR code is being generated
                progressBar.setVisibility(View.VISIBLE);

                // Call generateQrCode method from UtilViewModel
                utilViewModel.generateQrCode(qrCode);
            }
        });

        // Observe the QR code bitmap LiveData to update the ImageView
        utilViewModel.getQrCodeLiveData().observe(getViewLifecycleOwner(), qrBitmap -> {
            if (qrBitmap != null) {
                qrImageView.setImageBitmap(qrBitmap);
                progressBar.setVisibility(View.GONE); // Hide progress bar once QR code is generated
            }
        });

        // Observe for error messages
        utilViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                // Handle error here (e.g., show a Toast or display an error message)
                progressBar.setVisibility(View.GONE); // Hide progress bar in case of error
            }
        });

        // Observe loading state for user data
        userViewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Fetch user data when the fragment is created
        userViewModel.getUser();

        return rootView;
    }
}


