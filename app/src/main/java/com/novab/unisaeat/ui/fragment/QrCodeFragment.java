package com.novab.unisaeat.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.novab.unisaeat.R;
import com.novab.unisaeat.ui.viewmodel.UserViewModel;

public class QrCodeFragment extends Fragment {

    private UserViewModel userViewModel;

    public QrCodeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUser();


        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {

                String qrCode = user.getId() + "|" + user.getCf() + "|" + user.getToken();


                ImageView qrImageView = rootView.findViewById(R.id.qr_image_view);


                generateQrCode(qrCode, qrImageView);
            }
        });

        userViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                userViewModel.getUser();
            }
        });

        return rootView;
    }



    private void generateQrCode(String qrCode, ImageView qrImageView) {
        try {
            // Increase the size for a bigger QR code (e.g., 1024x1024)
            int width = 1024;
            int height = 1024;

            // Create a QR code writer
            QRCodeWriter writer = new QRCodeWriter();

            // Generate the QR code BitMatrix with the new size
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, width, height);

            // Create a bitmap with transparent background (ARGB_8888 supports transparency)
            android.graphics.Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            // Set all pixels to transparent first
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Set pixel to transparent if it's not part of the QR code, else set to black
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.TRANSPARENT);
                }
            }

            // Set the Bitmap as the ImageView source
            qrImageView.setImageBitmap(bitmap);

            // Ensure the background of the ImageView is transparent as well
            qrImageView.setBackgroundColor(Color.TRANSPARENT);

        } catch (Exception e) {
            // Handle exceptions (e.g., invalid QR code data)
            e.printStackTrace();
        }
    }

}
