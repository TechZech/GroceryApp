package com.groceryapp.upcdata.DB.User;

import com.google.firebase.auth.FirebaseAuth;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.Group.Settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String userID;
    private String Username;
    private String Email;
    private String photoUrl;



    private UserSettings UserSettings;

    public User(){
        userID = "";
        Username = "";
        Email = "";
        photoUrl = "";
        UserSettings = new UserSettings();
    }

    //This constructor will be used when creating a new user
    public User(String userid, String username, String email){
        userID = userid;
        Username = username;
        Email = email;
        photoUrl = "";
        UserSettings = new UserSettings();
    }
    public User(String userid, String username, String email, UserSettings userSettings){
        userID = userid;
        Username = username;
        Email = email;
        photoUrl = "";
        UserSettings = userSettings;
    }
    public User(FirebaseAuth firebaseAuth){
        userID = firebaseAuth.getCurrentUser().getUid();
        Username = firebaseAuth.getCurrentUser().getDisplayName();
        Email = firebaseAuth.getCurrentUser().getEmail();
        if (firebaseAuth.getCurrentUser().getPhotoUrl() == null) {
        }
        else {
            photoUrl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
        }
        UserSettings = new UserSettings();
    }
    public UserSettings getUserSettings() {
        return UserSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        UserSettings = userSettings;
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

    public String getProfilePhotoURL() {
        return photoUrl;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.photoUrl = profilePhotoURL;
    }
}
