package com.example.cyclerent;

import java.util.HashMap;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    String str = scannerView.cycleid;
    @POST("/users/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);
    // LoginResult -> return
    //  email aur password hashmap me store kiya hai

    @POST("/user/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

//    @GET(("/cycles/"+str)
        @GET("/cycles/{cycleid}")
        Call<Cycle> getCycle(@Path("cycleid") String cycleid);


//    @FormUrlEncoded
    @PATCH("/users/{id}")
    Call<Void> setRented(@Path("id") String _id,
                         @Body HashMap<String, String> map);

    @PATCH("/cycles/{cycleid}")
    Call<Void> setRentedUser(@Path("cycleid") String cycleid,
                         @Body HashMap<String, String> map);
//    @PATCH("cycles/{cycleid}")
//    Call<Cycle> getCycle(@Path("cycleid") String id);
}
