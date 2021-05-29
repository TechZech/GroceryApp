package com.groceryapp.upcdata.DB.GroceryItem;


import com.groceryapp.upcdata.DB.Friend.Comment;
import com.groceryapp.upcdata.DB.User.User;

import java.util.ArrayList;
import java.util.Date;

public class GroceryPost implements Comparable<GroceryPost>{
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    private double lat;
    private double lon;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    private String placeName;
    public User user = new User("test","test","test");
    public GroceryItem groceryItem;
    private Boolean whichList;

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

   public int numLikes = 0;
    private ArrayList<Comment> comments = new ArrayList<>();

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    private Date dateTime;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private String pid;
    public GroceryPost(String pid) {

        this.pid = pid;
    }

    public GroceryPost(GroceryItem gp, User u) {
        this.user = u;
        this.groceryItem = gp;
        this.whichList = gp.isInventory();
    }


    public GroceryPost(User user, GroceryItem groceryItem, Boolean whichList){
        this.user = user;
        this.groceryItem = groceryItem;
        this.whichList = whichList;
        dateTime = new Date();
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

    @Override
    public int compareTo(GroceryPost o) {
        Date compareDate = ((GroceryPost)o).getDateTime();
        return compareDate.compareTo(this.dateTime);
    }
}
