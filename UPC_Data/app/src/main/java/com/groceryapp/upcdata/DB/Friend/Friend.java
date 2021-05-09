package com.groceryapp.upcdata.DB.Friend;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DBHelper;

public class Friend {
    DBHelper dbHelper = new DBHelper();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private String email;
    public String username;
    private String userID;

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }


    public Friend(){
    }

    public Friend(String userID, String username, String email) {
        this.userID = userID;
        this.username = username;
        this.email = email;

        //    this.Username = dbHelper.getUser(uid).getUsername();
    }

    @Override
    public String toString() {
        return "Friend{" +
                ", userID='" + userID + '\'' +
                '}';
    }


    public String getuserID() {
        return userID;
    }

    public void setuserID(String userID) {
        this.userID = userID;
    }

    public String getusername(){
        return username;
    }
    public void setusername(String username){
        this.username = username;
    }
}