package com.groceryapp.upcdata.DB.User;

import com.groceryapp.upcdata.DB.Group.Settings;

import java.io.Serializable;

public class UserSettings implements Settings, Serializable {
    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }


    private String visibility;
    public UserSettings(){
        visibility = "Public";
    }
    public UserSettings(String visibility){
        this.visibility = visibility;
    }
}
