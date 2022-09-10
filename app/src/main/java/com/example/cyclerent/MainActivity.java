package com.example.cyclerent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";
    public static String AUTH_TOKEN = "";
    public static String userType = "";
    public static boolean addCycle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing

        // if login button is presses
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(MainActivity.this, AdminHome.class));
//                handleLoginDialog(); // defined below

            }
        });
        // if signup button is presses
//        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getCycleHandler("WIFI:S:Tatto;T:WPA;P:e98$ajewr;H:false;;");
//                handleSignupDialog(); // defined below
//                setRentedHandler("WIFI:S:Tatto;T:WPA;P:e98$ajewr;H:false;;");
//            }
//        });

    }

    private void handleLoginDialog() {

//        View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
//        // inflating loging_dialog.xml
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this); // alert type
//
//        builder.setView(view).show();

        TextView loginBtn = (TextView) findViewById(R.id.login);
        final EditText emailEdit = (EditText) findViewById(R.id.emailEditText);
        final EditText passwordEdit = (EditText) findViewById(R.id.passwordEditText);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Loging You In...",
                        Toast.LENGTH_SHORT).show();
                HashMap<String, String> map = new HashMap<>();
                // preparing for post
                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());
                // post request
                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                // execute http request
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.code() == 200) {
                            Toast.makeText(MainActivity.this, "Login Successfully",
                                    Toast.LENGTH_LONG).show();
                            LoginResult result = response.body();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setTitle(result.getMessage());
                            builder1.setMessage(result.getToken());
                            builder1.show();
                            AUTH_TOKEN = result.getToken();

                            if(result.getRole().equals("guard")){
                                userType = "guard";
//                                Toast.makeText(MainActivity.this, " Guard SAAB",
//                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, LoggedGuardActivity.class);
////                                Log.i("SURFYANSH", result.toString());
                                intent.putExtra("_id", result.get_id());
                                intent.putExtra("name", result.getName());
                                intent.putExtra("email", result.getEmail());
                                startActivity(intent);
                            }else {
                                userType = "student";
                                Intent intent = new Intent(MainActivity.this, LoggedUserActivity.class);
//                                Log.i("SURFYANSH", result.toString());
                                intent.putExtra("_id", result.get_id());
                                intent.putExtra("name", result.getName());
                                intent.putExtra("rollno", result.getRollno());
                                intent.putExtra("branch", result.getBranch());
                                intent.putExtra("email", result.getEmail());
                                if (!result.getCycleid().equals(""))
                                    intent.putExtra("cycleid", result.getCycleid());
                                else {
                                    intent.putExtra("cycleid", "Not Rented");
                                }
                                startActivity(intent);
                            }

                        } else if (response.code() == 404) {
                            Toast.makeText(MainActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

//    private void handleSignupDialog() {
//
//        View view = getLayoutInflater().inflate(R.layout.signup_dialog, null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(view).show();
//
//        Button signupBtn = view.findViewById(R.id.signup);
//        final EditText nameEdit = view.findViewById(R.id.nameEdit);
//        final EditText emailEdit = view.findViewById(R.id.emailEditText);
//        final EditText passwordEdit = view.findViewById(R.id.passwordEditText);
//
//        signupBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                HashMap<String, String> map = new HashMap<>();
//
////                map.put("name", nameEdit.getText().toString());
//                map.put("email", emailEdit.getText().toString());
//                map.put("password", passwordEdit.getText().toString());
//
//                Call<Void> call = retrofitInterface.executeSignup(map);
//
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//
//                        if (response.code() == 201) {
//                            Toast.makeText(MainActivity.this,
//                                    "Signed up successfully", Toast.LENGTH_LONG).show();
//                        } else if (response.code() == 409) {
//                            Toast.makeText(MainActivity.this,
//                                    "Already registered", Toast.LENGTH_LONG).show();
//                        }else{
//                            Toast.makeText(MainActivity.this,
//                                    "Some Error Occured", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(MainActivity.this, t.getMessage(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            }
//        });
//
//    }

    private void getCycleHandler(String cycleid) {
        Toast.makeText(MainActivity.this,"get cycle me ghusa", Toast.LENGTH_LONG).show();
        // post request
        Call<Cycle> call = retrofitInterface.getCycle("Bearer "+MainActivity.AUTH_TOKEN,cycleid);
        // execute http request
        call.enqueue(new Callback<Cycle>() {
            @Override
            public void onResponse(Call<Cycle> call, Response<Cycle> response) {

                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Login Successfully",
                            Toast.LENGTH_LONG).show();
                    Cycle result = response.body();

                    Intent intent = new Intent(MainActivity.this, LoggedUserActivity.class);
//                        Log.i("SURFYANSH",result.toString());
                    String _id = result.get_id();
                    String cycleid = result.getCycleid();
                    String status = result.getStatus();
                    String stdid = result.getStdid();
                    Toast.makeText(MainActivity.this, status,
                            Toast.LENGTH_LONG).show();
                    if (status.equals("")) {
                        Toast.makeText(MainActivity.this,"Nahi Hai", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MainActivity.this,"Pehle Se Rented Hai", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setTitle("This Cycle Is Already Rented");
                    }
                    startActivity(intent);


                } else if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(MainActivity.this, "Some Error Occured",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Cycle> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setRentedHandler(String cycleid) {

          Toast.makeText(MainActivity.this, "inside setRentedCycle",
                Toast.LENGTH_SHORT).show();
        HashMap<String, String> map = new HashMap<>();
        // preparing for post
        map.put("cycleid", cycleid);
//        // post request
        String str = "631a5a86bb3e663b7e6f1cab";
        Call<Void> call = retrofitInterface.setRented(str,map);
//        // execute http request
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Cycle Rented To You",
                            Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }



}
