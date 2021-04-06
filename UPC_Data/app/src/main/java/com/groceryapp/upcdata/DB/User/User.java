package com.groceryapp.upcdata.DB.User;

public class User {
    public User(String userName){
        this.setName(userName);
    }
    String name = "Test";
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
