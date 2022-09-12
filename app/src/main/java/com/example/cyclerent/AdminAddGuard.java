package com.example.cyclerent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.Guard;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminAddGuard extends AppCompatActivity {
    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_guard);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        addUserHandler();

    }
    private void addUserHandler() {


        LinearLayout addUserLL = (LinearLayout) findViewById(R.id.addGuardLL);
        final EditText nameGuardET =(EditText) findViewById(R.id.nameGuardET);
        final EditText emailGuardET =(EditText) findViewById(R.id.emailGuardET);
        final EditText passwordGuardET =(EditText) findViewById(R.id.passwordGuardET);

        addUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminAddGuard.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();
                HashMap<String, String> map = new HashMap<>();
                // preparing for post
                map.put("name", nameGuardET.getText().toString());
                map.put("password", passwordGuardET.getText().toString());
                map.put("email", emailGuardET.getText().toString());
                map.put("role", "guard");
                // post request
                Call<Void> call = retrofitInterface.executeSignup(map);
                // execute http request
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 201) {
                            Toast.makeText(AdminAddGuard.this, "Guard Added Successfully",
                                    Toast.LENGTH_LONG).show();
                            nameGuardET.setText("");
                            passwordGuardET.setText("");
                            emailGuardET.setText("");

                        } else {
                            Toast.makeText(AdminAddGuard.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AdminAddGuard.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}