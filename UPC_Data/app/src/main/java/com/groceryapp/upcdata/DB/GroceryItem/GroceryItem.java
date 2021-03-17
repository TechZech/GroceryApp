package com.groceryapp.upcdata.DB.GroceryItem;

public class GroceryItem {
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUpc() {
        return upc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    private int id;
        private String title;
        private String upc;
    public GroceryItem(){

    }

    @Override
    public String toString() {
        return "GroceryList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", upc='" + upc + '\'' +
                '}';
    }

    public GroceryItem(int id, String title, String upc) {
            this.id = id;
            this.title = title;
            this.upc = upc;

    }
}
