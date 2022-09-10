package com.example.cyclerent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoggedGuardActivity extends AppCompatActivity {

    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    boolean statusOfCycle = false;

    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";
    String _id;
    TextView scanbtn, returnbtn;
    LinearLayout scanLL, returnLL;
    public static TextView cycleidTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_guard);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing


        Intent intent = getIntent();

        _id = intent.getStringExtra("_id");
        String email = intent.getStringExtra("email");

        String name = intent.getStringExtra("name");

        TextView emailTV = (TextView) findViewById(R.id.emailTV);
        emailTV.setText(email);
        TextView nameTV = (TextView) findViewById(R.id.nameTV);
        nameTV.setText(name);
        cycleidTV = (TextView) findViewById(R.id.cycleidTV);
//        cycleidTV.setText(cycleid);

        returnbtn = (TextView) findViewById(R.id.returnbtn);


        returnLL = (LinearLayout) findViewById(R.id.returnLL);
        scanLL = (LinearLayout) findViewById(R.id.scanLL);


        returnLL.setVisibility(View.INVISIBLE);

        cycleidTV = (TextView) findViewById(R.id.cycleidTV);
        scanbtn = (TextView) findViewById(R.id.scanbtn);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoggedGuardActivity.this, "Button Dababa",
//                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoggedGuardActivity.this, scannerView.class));

                returnLL.setVisibility(View.VISIBLE);
                scanLL.setVisibility(View.INVISIBLE);
            }
        });

        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoggedGuardActivity.this, "Return Button is Pressed",
//                        Toast.LENGTH_LONG).show();

                String cycleid = (String) cycleidTV.getText();
//                Toast.makeText(LoggedGuardActivity.this, cycleid, Toast.LENGTH_LONG).show();

                if (!cycleid.equals(""))

                    getCycleHandler(cycleid);
////                if(statusOfCycle)


            }
        });
    }


    // Adding Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }
    //
//    }
    private void getCycleHandler(String cycleid) {
//        Toast.makeText(LoggedGuardActivity.this, "get cycle me ghusa", Toast.LENGTH_SHORT).show();
        // post request
        Call<Cycle> call = retrofitInterface.getCycle("Bearer "+MainActivity.AUTH_TOKEN,cycleid);
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
//                    _id = stdid;
//                    Toast.makeText(LoggedGuardActivity.this, status+" "+cycleid+" "+stdid,
//                            Toast.LENGTH_LONG).show();
                    if (status.equals("rented")) {
                        // AGAR RENTED NAHI HAI
//                        statusOfCycle = true;
                        scanLL.setVisibility(View.VISIBLE);
                        returnLL.setVisibility(View.INVISIBLE);

                        removeRentedHandler(stdid);
                        removeRentedUserHandler(cycleid);
//                        Toast.makeText(LoggedGuardActivity.this, "Rented Hai", Toast.LENGTH_SHORT).show();

                    } else {
//                        Toast.makeText(LoggedGuardActivity.this, "Cycle Rented Nahi Hai", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedGuardActivity.this);
                        builder.setTitle("ALERT");
                        builder.setMessage("This Cycle Is Not Rented Yet");
                        builder.show();

                        scanLL.setVisibility(View.VISIBLE);
                        returnLL.setVisibility(View.INVISIBLE);

                    }
//                    startActivity(intent);
                } else if (response.code() == 404) {
                    Toast.makeText(LoggedGuardActivity.this, "Wrong Credentials",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(LoggedGuardActivity.this, "Some Error Occured",
                            Toast.LENGTH_SHORT).show();
                }

                cycleidTV.setText(" Scan To Remove From Rent. ");
            }

            @Override
            public void onFailure(Call<Cycle> call, Throwable t) {
                Toast.makeText(LoggedGuardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    //
    private void removeRentedHandler(String stdid) {

//        Toast.makeText(LoggedGuardActivity.this, "inside setRentedCycle",
//                Toast.LENGTH_LONG).show();
        HashMap<String, String> map = new HashMap<>();
        // preparing for post
        map.put("cycleid", "");
//        // post request
        Call<Void> call = retrofitInterface.removeRented(stdid, map);
        // execute http request
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(LoggedGuardActivity.this, "Cycle Returned Successfully",
                            Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(LoggedGuardActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoggedGuardActivity.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoggedGuardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    //
    private void removeRentedUserHandler(final String cycleid) {

//        Toast.makeText(LoggedGuardActivity.this, "inside setRentedCycle",
//                Toast.LENGTH_LONG).show();
        HashMap<String, String> map = new HashMap<>();
        // preparing for post
        map.put("status", "");
        map.put("stdid", "");
//        // post request
        Call<Void> call = retrofitInterface.setRentedUser("Bearer "+MainActivity.AUTH_TOKEN,cycleid,map);
        // execute http request
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
//                    Toast.makeText(LoggedGuardActivity.this, "Jerk Off",
//                            Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(LoggedGuardActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoggedGuardActivity.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoggedGuardActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mailIt:
                  Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Write Subject Here");
                intent.putExtra(Intent.EXTRA_TEXT, "Write Your Feedback Here");

                startActivity(Intent.createChooser(intent, "Send Email"));
                Toast.makeText(this,"Generating Mail",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                MainActivity.AUTH_TOKEN = "";
                startActivity(new Intent(LoggedGuardActivity.this, MainActivity.class));

                Toast.makeText(this,"Logout Successfully",Toast.LENGTH_SHORT).show();
              return true;
            case R.id.appinfo:
                startActivity(new Intent(LoggedGuardActivity.this, AppInfo.class));
                Toast.makeText(this,"App Info",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}