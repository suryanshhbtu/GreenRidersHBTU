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

public class SignUpUser extends AppCompatActivity {

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
        addUserHandler();
    }
    private void addUserHandler() {


        LinearLayout addUserLL = (LinearLayout) findViewById(R.id.signUpLL);
        final EditText nameSignUpET = (EditText) findViewById(R.id.nameSignUpET);
        final EditText branchSignUpET = (EditText) findViewById(R.id.branchSignUpET);
        final EditText rollnoSignUpET = (EditText) findViewById(R.id.rollnoSignUpET);
        final EditText emailSignUpET = (EditText) findViewById(R.id.emailSignUpET);
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
}