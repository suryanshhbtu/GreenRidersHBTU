package com.example.GreenRidersHBTU.Scanners;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.example.GreenRidersHBTU.Admin.AdminDeleteCycle;
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

public class ScannerDeleteCycle extends AppCompatActivity implements ZXingScannerView.ResultHandler
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

        Toast.makeText(ScannerDeleteCycle.this,rawResult.getText(), Toast.LENGTH_SHORT).show();
        AdminDeleteCycle.qrDeleteTV.setText(rawResult.getText());
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