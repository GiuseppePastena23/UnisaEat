package com.novab.unisaeat.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

public class QrCodeFragment extends Fragment {

    private UserViewModel userViewModel;
    private ImageView qrImageView;
    private ProgressBar progressBar;

    public QrCodeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);
        qrImageView = rootView.findViewById(R.id.qr_image_view);
        progressBar = rootView.findViewById(R.id.progress_bar);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser(); // Request user data when the fragment is created

        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                String qrCode = user.getId() + ":" + user.getCf() + ":" + user.getToken();
                generateQrCode(qrCode, qrImageView);
                progressBar.setVisibility(View.GONE); // Hide progress bar when QR is generated
            }
        });

        userViewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE); // Show progress bar while loading data
            }
        });

        return rootView;
    }


    @Override
    public void onPause() {
        super.onPause();
        qrImageView.setImageBitmap(null); // Clear QR code when the fragment is paused
        progressBar.setVisibility(View.GONE); // Hide progress bar when the fragment is paused
    }




    private void generateQrCode(String qrCode, ImageView qrImageView) {
        try {
            int size = 1024;  // Size of the QR code

            // Create the QR code writer
            QRCodeWriter writer = new QRCodeWriter();

            // Generate the QR code matrix
            BitMatrix bitMatrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, size, size);

            // Create a bitmap and set pixels all at once
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // Set the Bitmap to the ImageView
            qrImageView.setImageBitmap(bitmap);
            qrImageView.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}