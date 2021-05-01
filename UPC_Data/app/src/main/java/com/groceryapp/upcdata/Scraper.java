package com.groceryapp.upcdata;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import androidx.fragment.app.Fragment;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;

import java.io.IOException;
import java.util.ArrayList;

public class Scraper {
    private String TAG = "Scraper";
    String upcCode;
    void setCode(String s) {
        this.upcCode = s;
    }
    String getCode() {
        return upcCode;
    }
    public String getUPCData(String query) throws IOException {
        Document doc = Jsoup.connect("https://www.barcodespider.com/"+query).get();
        //log(doc.html());

        Elements itemTitle = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.detailtitle").select("h2");
        Elements upcA = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.row");
        System.out.println(itemTitle.html());
        return itemTitle.html();

    }
    public String getUPCDatatest() throws IOException{
        Document doc = Jsoup.connect("https://www.upc-search.org/?q=016000124790").get();
        Elements itemTitle = doc.select("div#head");
        System.out.println(doc.html());
        return itemTitle.html();
    }
    public String getImageData(String query) throws IOException {
        Document doc = Jsoup.connect("https://www.barcodespider.com/"+query).get();
        //log(doc.html());
        Elements itemURL = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.row").select("div.col-md-7").select("div.barcode-detail-container").select("div.barcode-image-container").select("div.thumb-image").select("img");
        //System.out.println(itemURL.html());
        return itemURL.first().attr("src");
        //return itemURL.html();

    }

    public String getPriceData(String query) throws IOException{
        Document doc = Jsoup.connect("https://www.barcodespider.com/"+query).get();
        Elements rows = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.row").select("div.col-md-12.mt-3").select("div.store-list.pt-2.table-responsive").select("table.table.list").select("tbody").select("tr").select("td:contains($)");
        String itemPrc = "";
        itemPrc = rows.html();
        String[] arr = itemPrc.split(" ", 2);
        itemPrc = arr[0];
        return itemPrc;
    }

    public ArrayList<GroceryItem> getSimilarProducts(String query) throws IOException{
        //THIS FUNCTION GETS 3 SIMILAR PRODUCTS. If we want this to be more, just kind of like copy and past the blocks below and change variables so it makes sense and what not
        String tempTitle;
        String tempUPC;
        String tempURL;
        int tempQuantity = 1;
        String tempPrice = "$0.00";

        // Similar Product GroceryItems
        GroceryItem sp1 = new GroceryItem();
        GroceryItem sp2 = new GroceryItem();
        GroceryItem sp3 = new GroceryItem();

        ArrayList<GroceryItem> similarProducts = new ArrayList<>();
        Document doc = Jsoup.connect("https://www.barcodespider.com/"+query).get();

        Elements productTable = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12")
                .select("div.box-content").select("div.box").select("div.row").select("div.col-md-12").select("div.related-code").select("div.upc-list").select("ul");

        //elements[0] gets first item... and so on
        Elements elements = productTable.first().children();

        // Similar Product #1
        tempTitle = elements.get(0).select("div.UPCdetail").select("p").html();
        tempUPC = elements.get(0).select("div.UPCdetail").select("a").html();
        tempURL = elements.get(0).select("a.product").select("img").attr("src");
        Log.i(TAG, tempTitle);
        Log.i(TAG, tempUPC);
        Log.i(TAG, tempURL);
        sp1.setTitle(tempTitle);
        sp1.setUpc(tempUPC);
        sp1.setImageUrl(tempURL);
        sp1.setQuantity(tempQuantity);
        sp1.setPrice(tempPrice);
        similarProducts.add(sp1);

        // Similar Product #2
        tempTitle = elements.get(1).select("div.UPCdetail").select("p").html();
        tempUPC = elements.get(1).select("div.UPCdetail").select("a").html();
        tempURL = elements.get(1).select("a.product").select("img").attr("src");
        Log.i(TAG, tempTitle);
        Log.i(TAG, tempUPC);
        Log.i(TAG, tempURL);
        sp2.setTitle(tempTitle);
        sp2.setUpc(tempUPC);
        sp2.setImageUrl(tempURL);
        sp2.setQuantity(tempQuantity);
        sp2.setPrice(tempPrice);
        similarProducts.add(sp2);

        // Similar Product #3
        tempTitle = elements.get(2).select("div.UPCdetail").select("p").html();
        tempUPC = elements.get(2).select("div.UPCdetail").select("a").html();
        tempURL = elements.get(2).select("a.product").select("img").attr("src");
        Log.i(TAG, tempTitle);
        Log.i(TAG, tempUPC);
        Log.i(TAG, tempURL);
        sp3.setTitle(tempTitle);
        sp3.setUpc(tempUPC);
        sp3.setImageUrl(tempURL);
        sp3.setQuantity(tempQuantity);
        sp3.setPrice(tempPrice);
        similarProducts.add(sp3);

        return similarProducts;
    }

    public ArrayList<String> getAllData(String query) throws IOException{
        ArrayList<String> allData = new ArrayList<>();
        Document doc = Jsoup.connect("https://www.barcodespider.com/"+query).get();

        // SLOT 0: reserved for returning the barcode itself
        allData.add(query);

        // SLOT 1: reserved for returning the Item Title (getUPCData)
        Elements itemTitle = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.detailtitle").select("h2");
        Elements upcA = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.row");
        allData.add(itemTitle.html());

        // SLOT 2: reserved for returning the item's image url (getImagedata)
        Elements itemURL = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.row").select("div.col-md-7").select("div.barcode-detail-container").select("div.barcode-image-container").select("div.thumb-image").select("img");
        allData.add(itemURL.first().attr("src"));

        // SLOT 3: reserved for returning the item price
        Elements rows = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.row").select("div.col-md-12.mt-3").select("div.store-list.pt-2.table-responsive").select("table.table.list").select("tbody").select("tr").select("td:contains($)");
        String itemPrc = "";
        itemPrc = rows.html();
        String[] arr = itemPrc.split(" ", 2);
        itemPrc = arr[0];
        allData.add(itemPrc);

        return allData;
    }
}