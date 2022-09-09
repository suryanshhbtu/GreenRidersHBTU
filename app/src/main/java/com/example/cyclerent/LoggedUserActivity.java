package com.example.cyclerent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoggedUserActivity extends AppCompatActivity {


    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    boolean statusOfCycle = false;

    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";
    String _id;
    Button scanbtn, rentbtn;
    public static TextView cycleidTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_user);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing



        Intent intent = getIntent();

         _id = intent.getStringExtra("_id");
        String name = intent.getStringExtra("name");
        String rollno = intent.getStringExtra("rollno");
        String branch = intent.getStringExtra("branch");
        String email = intent.getStringExtra("email");
        String cycleid = intent.getStringExtra("cycleid");

        TextView nameTV = (TextView) findViewById(R.id.nameTV);
        nameTV.setText(name);
        TextView rollnoTV = (TextView) findViewById(R.id.rollnoTV);
        rollnoTV.setText(rollno);
        TextView branchTV = (TextView) findViewById(R.id.branchTV);
        branchTV.setText(branch);
        TextView emailTV = (TextView) findViewById(R.id.emailTV);
        emailTV.setText(email);
        cycleidTV = (TextView) findViewById(R.id.cycleidTV);
        cycleidTV.setText(cycleid);

        cycleidTV =(TextView)findViewById(R.id.cycleidTV);
        scanbtn=(Button) findViewById(R.id.scanbtn);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoggedUserActivity.this, "Button Dababa",
//                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoggedUserActivity.this,scannerView.class));
            }
        });

        rentbtn=(Button) findViewById(R.id.rentbtn);
        rentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoggedUserActivity.this, "Rented Button is Pressed",
//                        Toast.LENGTH_LONG).show();

                String cycleid = (String) cycleidTV.getText();
                Toast.makeText(LoggedUserActivity.this,cycleid, Toast.LENGTH_LONG).show();

                if(!cycleid.equals(""))

                getCycleHandler(cycleid);
//                if(statusOfCycle)


            }
        });

    }
    private void getCycleHandler(String cycleid) {
//        Toast.makeText(LoggedUserActivity.this,"get cycle me ghusa", Toast.LENGTH_LONG).show();
        // post request
        Call<Cycle> call = retrofitInterface.getCycle(cycleid);
        // execute http request
        call.enqueue(new Callback<Cycle>() {
            @Override
            public void onResponse(Call<Cycle> call, Response<Cycle> response) {

                if (response.code() == 200) {
                    Cycle result = response.body();

//                    Intent intent = new Intent(LoggedUserActivity.this, LoggedUserActivity.class);
//                        Log.i("SURFYANSH",result.toString());
//                    String _id = result.get_id();
                    String cycleid = result.getCycleid();
                    String status = result.getStatus();
                    String stdid = result.getStdid();
                    Toast.makeText(LoggedUserActivity.this, status,
                            Toast.LENGTH_LONG).show();
                    if (status.equals("")) {
                        // AGAR RENTED NAHI HAI
//                        statusOfCycle = true;
                        setRentedHandler(cycleid);
                        setRentedUserHandler(cycleid);
//                        Toast.makeText(LoggedUserActivity.this,"Nahi Hai", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LoggedUserActivity.this,"Already Rented", Toast.LENGTH_SHORT).show();
//                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoggedUserActivity.this);
//                        builder1.setTitle("This Cycle Is Already Rented");
                    }
//                    startActivity(intent);

                } else if (response.code() == 404) {
                    Toast.makeText(LoggedUserActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(LoggedUserActivity.this, "Some Error Occured",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Cycle> call, Throwable t) {
                Toast.makeText(LoggedUserActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setRentedHandler(String cycleid) {

        Toast.makeText(LoggedUserActivity.this, "Renting Cycle ...",
                Toast.LENGTH_LONG).show();
        HashMap<String, String> map = new HashMap<>();
        // preparing for post
        map.put("cycleid", cycleid);
//        // post request
        Call<Void> call = retrofitInterface.setRented(_id,map);
        // execute http request
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(LoggedUserActivity.this, "Cycle Rented To You",
                            Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(LoggedUserActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoggedUserActivity.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoggedUserActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setRentedUserHandler(String cycleid) {

        Toast.makeText(LoggedUserActivity.this, "inside setRentedCycle",
                Toast.LENGTH_LONG).show();
        HashMap<String, String> map = new HashMap<>();
        // preparing for post
        map.put("status", "rented");
        map.put("stdid", _id);
//        // post request
        Call<Void> call = retrofitInterface.setRentedUser(cycleid,map);
        // execute http request
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
//                    Toast.makeText(LoggedUserActivity.this, "Jerk Off",
//                            Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(LoggedUserActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoggedUserActivity.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoggedUserActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

}