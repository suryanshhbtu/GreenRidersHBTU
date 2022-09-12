package com.example.GreenRidersHBTU.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.GreenRidersHBTU.R;
import com.example.GreenRidersHBTU.RetrofitApiCalls.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminAddUser extends AppCompatActivity {

    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_user);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        addUserHandler();

    }
    private void addUserHandler() {


        LinearLayout addUserLL = (LinearLayout) findViewById(R.id.addUserLL);
        final EditText nameUserET =(EditText) findViewById(R.id.nameUserET);
        final EditText branchUserET =(EditText) findViewById(R.id.branchUserET);
        final EditText rollnoUserET =(EditText) findViewById(R.id.rollnoUserET);
        final EditText emailUserET =(EditText) findViewById(R.id.emailUserET);
        final EditText passwordUserET =(EditText) findViewById(R.id.passwordUserET);

        addUserLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdminAddUser.this, "Sending Data...",
                        Toast.LENGTH_SHORT).show();
                HashMap<String, String> map = new HashMap<>();
                // preparing for post
                map.put("name", nameUserET.getText().toString());
                map.put("password", passwordUserET.getText().toString());
                map.put("email", emailUserET.getText().toString());
                map.put("branch", branchUserET.getText().toString());
                map.put("rollno", rollnoUserET.getText().toString());
                map.put("cycleid", "");
                map.put("role", "student");
                // post request
                Call<Void> call = retrofitInterface.executeSignup(map);
                // execute http request
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 201) {
                            Toast.makeText(AdminAddUser.this, "User Added Successfully",
                                    Toast.LENGTH_LONG).show();
                            nameUserET.setText("");
                            passwordUserET.setText("");
                            emailUserET.setText("");
                            branchUserET.setText("");
                            rollnoUserET.setText("");

                        } else{
                            Toast.makeText(AdminAddUser.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(AdminAddUser.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}