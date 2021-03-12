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
import org.jsoup.select.Elements;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public class Scraper {
    String upcCode;
    void setCode(String s) {
        this.upcCode = s;
    }
    String getCode() {
        return upcCode;
    }
    String getUPCData(String query) throws IOException {
        Document doc = Jsoup.connect("https://www.barcodespider.com/"+query).get();
        //log(doc.html());

        Elements itemTitle = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.detailtitle").select("h2");
        Elements upcA = doc.select("div.main-content").select("section.body-content").select("div.container").select("div.row").select("div.col-md-12").select("div.box-content").select("div.box")
                .select("div.row");
        System.out.println(itemTitle.html());
        return itemTitle.html();

    }
    String getUPCDatatest() throws IOException{
        Document doc = Jsoup.connect("https://www.upc-search.org/?q=016000124790").get();
        Elements itemTitle = doc.select("div#head");
        System.out.println(doc.html());
        return itemTitle.html();
    }
}
