package com.example.GreenRidersHBTU;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpUser extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;


    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
//        addUserHandler();


        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }
    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            if(!account.getEmail().contains("@hbtu.ac.in")){

                Toast.makeText(SignUpUser.this, "Please Select A vaild HBTU Mail",
                        Toast.LENGTH_LONG).show();
                gotoMainActivity();
            }
            final TextView nameSignUpET = (TextView) findViewById(R.id.nameSignUpET);
            final TextView branchSignUpET = (TextView) findViewById(R.id.branchSignUpET);
            final TextView rollnoSignUpET = (TextView) findViewById(R.id.rollnoSignUpET);
            final TextView emailSignUpET = (TextView) findViewById(R.id.emailSignUpET);
            final EditText passwordSignUpET = (EditText) findViewById(R.id.passwordSignUpET);

            String name = "", branch = "";
            name = account.getDisplayName();
            rollnoSignUpET.setText(account.getEmail().substring(0,9));
            nameSignUpET.setText(name.substring(0,name.length()-31));
            emailSignUpET.setText(account.getEmail());
            passwordSignUpET.setText("Enter Your Password Here");

            String curr = account.getEmail().substring(4,6);
            switch(curr){
                case "01":
                    branch = "BE";
                    break;
                case "02":
                    branch = "Civil Engineering";
                    break;
                case "03":
                    branch = "Chemical Engineering";
                    break;
                case "04":
                    branch = "Computer Science Engineering";
                    break;
                case "05":
                    branch = "Electrical Engineering";
                    break;
                case "06":
                    branch = "Electronics Engineering";
                    break;
                case "07":
                    branch = "Food Technology";
                    break;
                case "08":
                    branch = "Information Technology";
                    break;
                case "09":
                    branch = "Leather Technology";
                    break;
                case "10":
                    branch = "Mechanical Engineering";
                    break;
                case "11":
                    branch = "Oil Technology";
                    break;
                case "12":
                    branch = "Paint And Leather";
                    break;
                case "13":
                    branch = "Paint Technology";
                    break;
            }
            branchSignUpET.setText(branch);

        }else{
            gotoMainActivity();
        }
    }

    private void gotoMainActivity(){
        Toast.makeText(SignUpUser.this, "Please Select A vaild",
                Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void addUserHandler() {


        LinearLayout addUserLL = (LinearLayout) findViewById(R.id.signUpLL);
        final TextView nameSignUpET = (TextView) findViewById(R.id.nameSignUpET);
        final TextView branchSignUpET = (TextView) findViewById(R.id.branchSignUpET);
        final TextView rollnoSignUpET = (TextView) findViewById(R.id.rollnoSignUpET);
        final TextView emailSignUpET = (TextView) findViewById(R.id.emailSignUpET);
        final EditText passwordSignUpET = (EditText) findViewById(R.id.passwordSignUpET);

        addUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpUser.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();
                HashMap<String, String> map = new HashMap<>();
                // preparing for post
                map.put("name", nameSignUpET.getText().toString());
                map.put("password", passwordSignUpET.getText().toString());
                map.put("email", emailSignUpET.getText().toString());
                map.put("branch", branchSignUpET.getText().toString());
                map.put("rollno", rollnoSignUpET.getText().toString());
                map.put("cycleid", "");
                map.put("role", "student");
                // post request
                Call<Void> call = retrofitInterface.executeSignup(map);
                // execute http request
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 201) {
                            Toast.makeText(SignUpUser.this, "Sign Up Successfully",
                                    Toast.LENGTH_LONG).show();
                            nameSignUpET.setText("");
                            passwordSignUpET.setText("");
                            emailSignUpET.setText("");
                            branchSignUpET.setText("");
                            rollnoSignUpET.setText("");

                        } else {
                            Toast.makeText(SignUpUser.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(SignUpUser.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}