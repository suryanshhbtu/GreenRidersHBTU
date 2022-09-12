package com.example.cyclerent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

public class LoggedUserActivity extends AppCompatActivity {


    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    boolean statusOfCycle = false;

    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";
    String _id;
    TextView scanbtn, rentbtn;
    LinearLayout scanLL , rentLL;

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

        scanLL = (LinearLayout) findViewById(R.id.scanLL);
        rentLL = (LinearLayout) findViewById(R.id.rentLL);
        cycleidTV =(TextView)findViewById(R.id.cycleidTV);
        scanbtn=(TextView) findViewById(R.id.scanbtn);

        rentbtn=(TextView) findViewById(R.id.rentbtn);
        if(!cycleidTV.getText().equals("Not Rented")){
            scanbtn.setVisibility(View.INVISIBLE);
            scanLL.setVisibility(View.INVISIBLE);
        }

        rentbtn.setVisibility(View.INVISIBLE);
        rentLL.setVisibility(View.INVISIBLE);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(LoggedUserActivity.this, "Button Dababa",
//                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoggedUserActivity.this,scannerView.class));
                rentbtn.setVisibility(View.VISIBLE);
                scanbtn.setVisibility(View.INVISIBLE);
                rentLL.setVisibility(View.VISIBLE);
                scanLL.setVisibility(View.INVISIBLE);
            }
        });

        rentbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                rentbtn.setVisibility(View.VISIBLE);
                rentLL.setVisibility(View.VISIBLE);
//                Toast.makeText(LoggedUserActivity.this, "Rented Button is Pressed",
//                        Toast.LENGTH_LONG).show();

                String cycleid = (String) cycleidTV.getText();
//                Toast.makeText(LoggedUserActivity.this,cycleid, Toast.LENGTH_LONG).show();

                if(!cycleid.equals(""))
                    getCycleHandler(cycleid);
//                if(statusOfCycle)


            }
        });

    }
    @Override
    public void onBackPressed() {
//            super.onBackPressed();
        finishAffinity();
        finish();
    }

    private void getCycleHandler(String cycleid) {
//        Toast.makeText(LoggedUserActivity.this,"get cycle me ghusa", Toast.LENGTH_LONG).show();
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
//                    Toast.makeText(LoggedUserActivity.this, status,
//                            Toast.LENGTH_LONG).show();
                    if (status.equals("")) {
                        // AGAR RENTED NAHI HAI
//                        statusOfCycle = true;
                        setRentedHandler(cycleid);
                        setRentedUserHandler(cycleid);
                        rentbtn.setVisibility(View.INVISIBLE);
                        rentLL.setVisibility(View.INVISIBLE);
                        scanbtn.setVisibility(View.VISIBLE);
                        rentbtn.setVisibility(View.INVISIBLE);
//                        Toast.makeText(LoggedUserActivity.this,"Nahi Hai", Toast.LENGTH_SHORT).show();

                    } else {
//                        Toast.makeText(LoggedUserActivity.this,"Already Rented", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedUserActivity.this);
                        builder.setTitle("ALERT");
                        builder.setMessage("This Cycle Is Already Rented.");
                        builder.show();
                        cycleidTV.setText("Not Rented");
                        scanbtn.setVisibility(View.VISIBLE);
                        rentbtn.setVisibility(View.INVISIBLE);
                        scanLL.setVisibility(View.VISIBLE);
                        rentLL.setVisibility(View.INVISIBLE);
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

//        Toast.makeText(LoggedUserActivity.this, "Renting Cycle ...",
//                Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoggedUserActivity.this, "Cycle Is Rented To You",
                            Toast.LENGTH_SHORT).show();
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

//        Toast.makeText(LoggedUserActivity.this, "inside setRentedCycle",
//                Toast.LENGTH_LONG).show();
        HashMap<String, String> map = new HashMap<>();
        // preparing for post
        map.put("status", "rented");
        map.put("stdid", _id);
//        // post request
        Call<Void> call = retrofitInterface.setRentedUser("Bearer "+MainActivity.AUTH_TOKEN,cycleid,map);
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

    // Adding Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.mailIt:

                Toast.makeText(this,"Generating Mail",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Write Subject Here");
                intent.putExtra(Intent.EXTRA_TEXT, "Write Your Feedback Here");

                return true;
            case R.id.logout:
                MainActivity.AUTH_TOKEN = "";
                 startActivity(new Intent(LoggedUserActivity.this, MainActivity.class));
                SharedPreferences preferences =getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Toast.makeText(this,"Logout Successfully",Toast.LENGTH_SHORT).show();

                return true;
             case R.id.appinfo:
                startActivity(new Intent(LoggedUserActivity.this, AppInfo.class));
                Toast.makeText(this,"Loading Info",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.changePasswordMenu:
                startActivity(new Intent(LoggedUserActivity.this, ChangePassword.class));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}