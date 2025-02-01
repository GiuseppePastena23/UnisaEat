package com.novab.unisaeat.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.novab.unisaeat.R;

public class ScanViewModel extends AndroidViewModel {

    private final MutableLiveData<String> scanResult = new MutableLiveData<>();


    public ScanViewModel(Application application) {
        super(application);
    }

    public void setScanResult(String result) {
        scanResult.setValue(result);
    }

    public LiveData<String> getScanResult() {
        return scanResult;
    }

    public void startQrCodeScanner(ActivityResultLauncher<ScanOptions> launcher, Context context) {
        ScanOptions options = new ScanOptions();
        options.setPrompt(context.getString(R.string.scan_prompt));
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CaptureActivity.class);

        launcher.launch(options);
    }
}
