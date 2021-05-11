package com.groceryapp.upcdata.DB.Group;

import com.groceryapp.upcdata.DB.User.User;

public class GroupSettings implements Settings {
    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getWhoCanPost() {
        return whoCanPost;
    }

    public void setWhoCanPost(String whoCanPost) {
        this.whoCanPost = whoCanPost;
    }

    private String visibility = "Public";
    private String whoCanPost = "Everyone";
    public GroupSettings(){
        visibility = "Public";
        whoCanPost = "Everyone";
    }
    public GroupSettings(String visibility, String whoCanPost){
    this.visibility = visibility;
    this.whoCanPost = whoCanPost;
    }

}
