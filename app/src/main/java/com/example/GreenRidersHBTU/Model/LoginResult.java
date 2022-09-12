package com.example.GreenRidersHBTU.Model;

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

    public LoginResult(String message, String token, String _id, String email, String password, String name, String rollno, String branch, String cycleid, String role) {
        this.message = message;
        this.token = token;
        this._id = _id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.rollno = rollno;
        this.branch = branch;
        this.cycleid = cycleid;
        this.role = role;
    }

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
