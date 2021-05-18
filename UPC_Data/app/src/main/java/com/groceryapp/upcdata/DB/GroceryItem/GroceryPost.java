package com.groceryapp.upcdata.DB.GroceryItem;


import com.groceryapp.upcdata.DB.User.User;

import java.util.Date;

public class GroceryPost {
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    private String placeid;
    public User user = new User("test","test","test");
    public GroceryItem groceryItem;
    private Boolean whichList;

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    private Date dateTime;

    public GroceryPost(GroceryItem gp, User u) {
        this.user = u;
        this.groceryItem = gp;
        this.whichList = gp.isInventory();
    }


    public GroceryPost(User user, GroceryItem groceryItem, Boolean whichList){
        this.user = user;
        this.groceryItem = groceryItem;
        this.whichList = whichList;
    }

    public GroceryPost(){
        User u = new User();
        this.user = u;
        GroceryItem g = new GroceryItem();
        this.groceryItem = g;
        this.whichList = true;
    }


    public Boolean getWhichList() {
        return whichList;
    }

    public GroceryItem getGroceryItem() {
        return groceryItem;
    }

    public void setGroceryItem(GroceryItem groceryItem) {
        this.groceryItem = groceryItem;
    }

    public void setWhichList(Boolean whichList) {
        this.whichList = whichList;
    }
}
