package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.novab.unisaeat.data.repository.UtilRepository;

public class UtilViewModel extends AndroidViewModel {

    private final UtilRepository utilRepository;
    private final MutableLiveData<String> imagesBase64LiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> qrCodeLiveData = new MutableLiveData<>();

    public UtilViewModel(Application application) {
        super(application);
        utilRepository = new UtilRepository();
    }


    private void getMenu(int day) {
        isLoadingLiveData.setValue(true);

        utilRepository.getMenu(day, new UtilRepository.MenuCallback() {
            @Override
            public void onSuccess(String imgBase64) {
                imagesBase64LiveData.setValue(imgBase64);
                isLoadingLiveData.setValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.setValue(errorMessage);
                isLoadingLiveData.setValue(false);
            }
        });
    }

    public void generateQrCode(String qrCode) {
        // Run the QR code generation on a background thread
        new Thread(() -> {
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

                // Post the result back to the main thread
                new Handler(Looper.getMainLooper()).post(() -> {
                    qrCodeLiveData.setValue(bitmap);  // Update LiveData with the generated QR code bitmap
                });
            } catch (Exception e) {
                e.printStackTrace();
                // If there is an error, post it to errorLiveData
                new Handler(Looper.getMainLooper()).post(() -> {
                    errorLiveData.setValue("QR Code generation failed");
                });
            }
        }).start();
    }

    ///////////////////////////////////////////////////////////////////////////

    public void getDayMenu(int day) {
        getMenu(day);
    }

    public LiveData<Bitmap> getQrCodeLiveData() {
        return qrCodeLiveData;
    }

    public LiveData<String> getImagesBase64LiveData() {
        return imagesBase64LiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }
}
