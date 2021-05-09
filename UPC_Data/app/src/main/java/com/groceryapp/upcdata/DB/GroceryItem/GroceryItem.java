package com.groceryapp.upcdata.DB.GroceryItem;

import android.util.Log;

public class GroceryItem {
    private String title;
    private String upc;
    private int quantity;
    private String ImageUrl;
    private String price;
    private boolean isInventory;


    public GroceryItem(){
        this.title = "";
        this.upc = "";
        this.ImageUrl = "";
        this.quantity = 0;
        this.price = "";
        this.isInventory = true;
    }




    public GroceryItem(String title, String upc, String imageUrl, int quantity, String price, boolean isInventory) {
        this.title = title;
        this.upc = upc;
        this.ImageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
        this.isInventory = isInventory;
    }

    @Override
    public String toString() {
        return "GroceryList{" +
                ", title='" + title + '\'' +
                ", upc='" + upc + '\'' +
                '}';
    }


    public String getTitle() {
        return title;
    }

    public String getUpc() {
        return upc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public boolean isInventory() {
        return isInventory;
    }

    public void setInventory(boolean inventory) {
        isInventory = inventory;
    }

    public double returnPriceAsFloat(){
        String thisPrice = this.price;
        Log.d("GroceryItem", thisPrice.substring(1));
        return Double.parseDouble(thisPrice.substring(1));
    }
}
