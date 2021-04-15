package com.groceryapp.upcdata.DB.GroceryItem;

public class GroceryItem {
    private String title;
    private String upc;
    private int quantity;
    private String ImageUrl;



    public GroceryItem(){
    }

    public GroceryItem(String title, String upc, String imageUrl) {
        this.title = title;
        this.upc = upc;
        this.quantity = 0;
        this.ImageUrl = imageUrl;
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
}
