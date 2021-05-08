package com.groceryapp.upcdata.DB.User;

import com.google.firebase.auth.FirebaseAuth;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userID;
    private String Username;
    private String Email;


    public User(){}

    //This constructor will be used when creating a new user
    public User(String userid, String username, String email){
        userID = userid;
        Username = username;
        Email = email;
    }

    public User(FirebaseAuth firebaseAuth){
        userID = firebaseAuth.getCurrentUser().getUid();
        Username = firebaseAuth.getCurrentUser().getDisplayName();
        Email = firebaseAuth.getCurrentUser().getEmail();
    }


    public void setUserID(String userid){
        userID = userid;
    }
    public String getUserID(){
        return userID;
    }

    public void setUsername(String name){
        Username = name;
    }
    public String getUsername(){
        return Username;
    }

    public void setEmail(String email){
        Email = email;
    }
    public String getEmail(){
        return Email;
    }
}
