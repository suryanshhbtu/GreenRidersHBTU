package com.example.cyclerent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminDeleteUserAndGuard extends AppCompatActivity {
    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_delete_user_and_guard);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        addUserHandler();

    }
    private void addUserHandler() {


        LinearLayout deleteBtnLL = (LinearLayout) findViewById(R.id.deleteBtnLL);
        final EditText emailUserET =(EditText) findViewById(R.id.emailDeleteET);

        deleteBtnLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminDeleteUserAndGuard.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();
                // post request
                Call<Void> call = retrofitInterface.executeDelete(emailUserET.getText().toString());
                // execute http request
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            Toast.makeText(AdminDeleteUserAndGuard.this, "User Deleted Successfully",
                                    Toast.LENGTH_LONG).show();
                            emailUserET.setText("");

                        } else if (response.code() == 404) {
                            Toast.makeText(AdminDeleteUserAndGuard.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            {
                                Toast.makeText(AdminDeleteUserAndGuard.this, "Some Error Occured",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AdminDeleteUserAndGuard.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}