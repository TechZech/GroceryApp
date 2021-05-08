package com.groceryapp.upcdata.DB.User;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DBHelper;

public class Group {

    private String groupname;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User[] getMembers() {
        return members;
    }

    public void setMembers(User[] members) {
        this.members = members;
    }



    private User owner;
    private User[] members;



    public Group(){
    }
    public Group(String groupname, User owner) {
        this.groupname = groupname;
        this.owner = owner;
        this.members = members;
    }
    public Group(String groupname, User owner, User[] members) {
        this.groupname = groupname;
        this.owner = owner;
        this.members = members;
    }

    @Override
    public String toString() {
        return "Group{" +
                ", owner='" + owner + '\'' +
                '}';
    }



}