package com.groceryapp.upcdata.DB.User;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.groceryapp.upcdata.DBHelper;

public class FriendRequest {
    DBHelper dbHelper = new DBHelper();

    private String uid;
    private String Username;
    public final String TAG = "Friend Request";

        public FriendRequest(){
        }

        public FriendRequest(String uid) {
            this.uid = uid;
            //this.Username = dbHelper.getUser(uid).getUsername();
        }

        @Override
        public String toString() {
            return "FriendRequest{" +
                    ", uid='" + uid + '\'' +
                    '}';
        }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername(){
            return Username;
    }
    public void setUsername(String Username){
            this.Username = Username;
    }
}
