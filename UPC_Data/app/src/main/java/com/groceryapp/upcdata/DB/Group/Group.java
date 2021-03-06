package com.groceryapp.upcdata.DB.Group;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;

import java.util.ArrayList;

public class Group {

    private String groupname;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    private String photoUrl;
    public GroupSettings getGroupSettings() {
        return groupSettings;
    }

    public void setGroupSettings(GroupSettings groupSettings) {
        this.groupSettings = groupSettings;
    }

    private GroupSettings groupSettings;
    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    private String gid;

    public Group(Group gid) {
        this.groupname = gid.groupname;
        this.owner = gid.owner;
        this.members = gid.members;
        this.photoUrl = null;
        this.groupSettings = new GroupSettings();
    }

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


    private User owner;

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    private ArrayList<User> members = new ArrayList<User>();



    public Group(){
    }
    public Group(String groupname, User owner) {
        this.groupname = groupname;
        this.owner = owner;
        members.add(owner);
        this.groupSettings = new GroupSettings();
        this.photoUrl = null;

    }
    public Group(String groupname, User owner, ArrayList<User> members) {
        this.groupname = groupname;
        this.owner = owner;
        this.members = members;
        this.groupSettings = new GroupSettings();
        this.photoUrl = null;
    }
    public Group(String groupname, GroupSettings groupSettings) {
        this.groupname = groupname;
        this.owner = owner;
        this.members = members;
        this.groupSettings = groupSettings;
        this.photoUrl = null;
    }
    public Group(String groupname, User owner, GroupSettings groupSettings) {
        this.groupname = groupname;
        this.owner = owner;
        this.members = members;
        this.groupSettings = groupSettings;
        this.photoUrl = null;
    }
    public Group(String groupname, User owner, ArrayList<User> members, GroupSettings groupSettings) {
        this.groupname = groupname;
        this.owner = owner;
        this.members = members;
        this.groupSettings = groupSettings;
        this.photoUrl = null;
    }
    @Override
    public String toString() {
        return "Group{" +
                ", owner='" + owner + '\'' +
                '}';
    }



}