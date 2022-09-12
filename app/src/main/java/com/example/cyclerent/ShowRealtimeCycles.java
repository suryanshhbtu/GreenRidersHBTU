package com.example.cyclerent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ShowRealtimeCycles extends AppCompatActivity {
    private Retrofit retrofit;  // global variable of retrofit class
    private RetrofitInterface retrofitInterface; // global variable of retrofit Interface
    private String BASE_URL = "https://pacific-fortress-54764.herokuapp.com";
    String _id;
    ListView l;
    String tutorials[]
            = { "Algorithms", "Data Structures",
            "Languages", "Interview Corner",
            "GATE", "ISRO CS",
            "UGC NET CS", "CS Subjects",
            "Web Technologies" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // above defined
                .addConverterFactory(GsonConverterFactory.create()) // json -> javaObject
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class); // instantinsing
        getRentedCycle();
        l = findViewById(R.id.list);


    }
    private void getRentedCycle() {

//        // post request
        Call<List<Cycle>> call = retrofitInterface.getRentedCycle("Bearer "+MainActivity.AUTH_TOKEN);
        // execute http request
        call.enqueue(new Callback<List<Cycle>>() {
            @Override
            public void onResponse(Call<List<Cycle>> call, Response<List<Cycle>> response) {

                if (response.code() == 200) {
                    Toast.makeText(ShowRealtimeCycles.this, "Loaded Realtime Rented Cycles Successfully",
                            Toast.LENGTH_LONG).show();
                   List<Cycle> result = response.body();
                    String cycles[] = new String[result.size()];
                    for(int i = 0;i<result.size();i++){
                       cycles[i] = result.get(i).getCycleid();
                   }
                    ArrayAdapter<String> arr;
                    arr= new ArrayAdapter<String>(ShowRealtimeCycles.this,R.layout.support_simple_spinner_dropdown_item,
                            cycles);
                    l.setAdapter(arr);
//                    WordAdapter family=new WordAdapter(ShowRealtimeCycles.this,result);
//                    ListView listView=(ListView)findViewById(R.id.list);
//                    listView.setAdapter(family);
                } else if (response.code() == 404) {
                    Toast.makeText(ShowRealtimeCycles.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ShowRealtimeCycles.this, "Some Error in Patch",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cycle>> call, Throwable t) {
                Toast.makeText(ShowRealtimeCycles.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}