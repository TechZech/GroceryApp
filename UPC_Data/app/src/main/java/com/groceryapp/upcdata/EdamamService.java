package com.groceryapp.upcdata;


import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.gson.JsonObject;
import com.groceryapp.upcdata.DB.GroceryItem.NutritionData;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import okhttp3.Headers;


public class EdamamService {
    public final String TAG = "EdamamService";

    public String EDAMAM_ID = BuildConfig.EDAMAM_ID;
    public static final String EDAMAM_KEY = BuildConfig.EDAMAM_KEY;
    public static final String EDAMAM_BASE_URL = "https://api.edamam.com/api/food-database/v2/parser";

    public interface EdamamCallback {
        void onResponse(NutritionData nutritionData);
    }

    public void findItemFromUPC(String query, EdamamCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(EDAMAM_BASE_URL + "?upc=" + query + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                NutritionData nutritionData;
                try {
                    JSONArray Array = jsonObject.getJSONArray("hints");
                    JSONObject nutritionDataJson = Array.getJSONObject(0).getJSONObject("food").getJSONObject("nutrients");
                    Log.i(TAG, "Food Nutrition: " + nutritionDataJson.toString());
                    nutritionData = new NutritionData(nutritionDataJson.getString("ENERC_KCAL"), nutritionDataJson.getString("FAT"),
                            nutritionDataJson.getString("FASAT"), nutritionDataJson.getString("FATRN") ,nutritionDataJson.getString("CHOLE"),
                            nutritionDataJson.getString("CHOCDF"), nutritionDataJson.getString("NA"), nutritionDataJson.getString("FIBTG"),
                            nutritionDataJson.getString("SUGAR"),nutritionDataJson.getString("PROCNT"));
                    callback.onResponse(nutritionData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

}
