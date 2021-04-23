package com.groceryapp.upcdata.DB.GroceryItem;

public class GroceryItem {
    private String title;
    private String upc;
    private int quantity;
    private String ImageUrl;
    private String price;



    public GroceryItem(){
    }

    public GroceryItem(String title, String upc, String imageUrl, int quantity) {
        this.title = title;
        this.upc = upc;
        this.quantity = quantity;
        this.ImageUrl = imageUrl;
        this.price = "$10.00";
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
}
