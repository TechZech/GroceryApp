package com.groceryapp.upcdata.DB.User;

import com.groceryapp.upcdata.DB.Group.Settings;

public class UserSettings implements Settings {
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
