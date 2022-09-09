package com.example.cyclerent;

import com.google.gson.annotations.SerializedName;

public class LoginResult {

//    @SerializedName("message") // key hai message
//    yahan pr same hai iss liye @SerializedName(..) ki zarurat nahi
    private String message;

    private String token;
    private String _id;
    private String email;
    private String password;
    private String name;
    private String rollno;
    private String branch;
    private String cycleid;
    private String role;



    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRollno() {
        return rollno;
    }

    public String getBranch() {
        return branch;
    }

    public String getCycleid() {
        return cycleid;
    }
}
