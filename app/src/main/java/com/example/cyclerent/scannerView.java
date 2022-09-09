package com.example.cyclerent;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class scannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler
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
            if(!MainActivity.userType.equals("guard")){
                LoggedUserActivity.cycleidTV.setText(rawResult.getText());
            }else{
                LoggedGuardActivity.cycleidTV.setText(rawResult.getText());
            }

            cycleid = rawResult.getText();
//            getCycleHandler(cycleid);
            onBackPressed();
        }

        private void getCycleHandler(String cycleid) {
            Toast.makeText(com.example.cyclerent.scannerView.this,"get cycle me ghusa", Toast.LENGTH_LONG).show();
            // post request
            Call<Cycle> call = retrofitInterface.getCycle(cycleid);
            // execute http request
            call.enqueue(new Callback<Cycle>() {
                @Override
                public void onResponse(Call<Cycle> call, Response<Cycle> response) {

                    if (response.code() == 200) {
                        Toast.makeText(scannerView.this, "Login Successfully",
                                Toast.LENGTH_LONG).show();
                        Cycle result = response.body();

                        Intent intent = new Intent(scannerView.this, LoggedUserActivity.class);
//                        Log.i("SURFYANSH",result.toString());
                        String _id = result.get_id();
                        String cycleid = result.getCycleid();
                        String status = result.getStatus();
                        String stdid = result.getStdid();

                        if (status.equals("")) {
                            Toast.makeText(com.example.cyclerent.scannerView.this,"Nahi Hai", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(com.example.cyclerent.scannerView.this,"Pehle Se Rented Hai", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(scannerView.this);
                            builder1.setTitle("This Cycle Is Already Rented");
                        }
                        startActivity(intent);


                    } else if (response.code() == 404) {
                        Toast.makeText(scannerView.this, "Wrong Credentials",
                                Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<Cycle> call, Throwable t) {
                    Toast.makeText(scannerView.this, t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

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