package com.groceryapp.upcdata.DB.GroceryItem;


import com.groceryapp.upcdata.DB.User.User;

public class GroceryPost {
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User user = new User("test","test","test");
    public GroceryItem groceryItem;
    private Boolean whichList;

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

    public GroceryPost(){}


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
