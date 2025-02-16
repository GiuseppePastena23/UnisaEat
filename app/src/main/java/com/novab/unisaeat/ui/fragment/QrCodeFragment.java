package com.novab.unisaeat.ui.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
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
    private RelativeLayout qrContainer;

    private ObjectAnimator animator;

    public QrCodeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);
        qrImageView = rootView.findViewById(R.id.qr_image_view);
        progressBar = rootView.findViewById(R.id.progress_bar);
        qrContainer = rootView.findViewById(R.id.qr_container);


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser();


        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {

                String qrCode = user.getId() + ":" + user.getCf() + ":" + user.getToken();
                generateQrCode(qrCode, qrImageView);
                progressBar.setVisibility(View.GONE);
            }
        });

        userViewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Apply border animation

        return rootView;
    }


    private void animateBorder(View view) {
        // Get the background of the view as a GradientDrawable
        GradientDrawable drawable = (GradientDrawable) view.getBackground();

        // Define the start and end colors
        int startColor = ContextCompat.getColor(requireContext(), R.color.primary);
        int endColor = ContextCompat.getColor(requireContext(), R.color.light_blue_50);

        // Create the ValueAnimator to animate the stroke color
        ValueAnimator strokeColorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);

        // Set the duration, repeat count, and repeat mode
        strokeColorAnimator.setDuration(1500);
        strokeColorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        strokeColorAnimator.setRepeatMode(ValueAnimator.REVERSE);

        // Add an update listener to apply the animated stroke color to the drawable
        strokeColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get the animated color
                int color = (int) animation.getAnimatedValue();

                // Update the stroke color on the drawable
                drawable.setStroke(10, color); // Adjust the stroke width as needed
            }
        });

        // Start the animation
        strokeColorAnimator.start();
    }


    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    private void generateQrCode(String qrCode, ImageView qrImageView) {
        try {
            int size = 1024;
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, size, size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.TRANSPARENT);
                }
            }

            qrImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }


}
