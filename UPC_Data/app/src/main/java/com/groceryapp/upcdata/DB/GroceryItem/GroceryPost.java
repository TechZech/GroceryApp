package com.groceryapp.upcdata.DB.GroceryItem;


public class GroceryPost {
    private String UserName;
    private GroceryItem groceryItem;
    private Boolean whichList;

    public GroceryPost(String Username, GroceryItem groceryItem, Boolean whichList){
        this.UserName = Username;
        this.groceryItem = groceryItem;
        this.whichList = whichList;
    }

    public GroceryPost(){}

    public String getUserName() {
        return UserName;
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

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setWhichList(Boolean whichList) {
        this.whichList = whichList;
    }
}
