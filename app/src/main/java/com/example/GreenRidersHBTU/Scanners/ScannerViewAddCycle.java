package com.example.GreenRidersHBTU.Scanners;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.example.GreenRidersHBTU.Admin.AdminAddCycle;
import com.example.GreenRidersHBTU.MainActivity;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Retrofit;

public class ScannerViewAddCycle extends AppCompatActivity implements ZXingScannerView.ResultHandler
{

    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface

    ZXingScannerView scannerView;
    public static String cycleid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void handleResult(Result rawResult) {

        Toast.makeText(ScannerViewAddCycle.this,rawResult.getText(), Toast.LENGTH_SHORT).show();
        AdminAddCycle.qrTV.setText(rawResult.getText());
        MainActivity.addCycle = false;
        cycleid = rawResult.getText();
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}