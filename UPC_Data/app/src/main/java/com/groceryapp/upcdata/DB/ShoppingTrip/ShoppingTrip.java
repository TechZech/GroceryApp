package com.groceryapp.upcdata.DB.ShoppingTrip;

import java.util.Date;

public class ShoppingTrip implements Comparable<ShoppingTrip>{
    private Date date;
    private double totalPrice;
    private


    ShoppingTrip(){}

    public ShoppingTrip(double totalPrice) {
        this.date = new Date();
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int compareTo(ShoppingTrip shoppingTrip){
        return getDate().compareTo(shoppingTrip.getDate());
    }
}
