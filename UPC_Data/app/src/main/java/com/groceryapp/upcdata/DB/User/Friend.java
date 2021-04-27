package com.groceryapp.upcdata.DB.User;

public class Friend {
    private String uid;



    public Friend(){
    }

    public Friend(String uid) {
        this.uid = uid;
    }
/*
    @Override
    public String toString() {
        return "GroceryList{" +
                ", title='" + title + '\'' +
                ", upc='" + upc + '\'' +
                '}';
    }*/


    public String getUid(){
        return this.uid;
    }
}
