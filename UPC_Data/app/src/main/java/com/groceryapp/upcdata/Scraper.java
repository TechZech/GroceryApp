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

import java.io.IOException;
import java.util.ArrayList;

public class Scraper {
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
                .select("div.row").select("div.col-md-12 mt-3").select("div.store-list pt-2 table-responsive").select("table.table list").select("tr");
        String itemPrc = "";
        for (Element row : rows){
            if (row.select("td").contains('$')){
                itemPrc = row.select("td").text();
            }
        }
        return itemPrc;
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

        // SLOT 3: reserved for returning the item price (will do eventually)

        return allData;
    }
}