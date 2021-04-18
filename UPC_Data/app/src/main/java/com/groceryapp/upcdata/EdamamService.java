package com.groceryapp.upcdata;


import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.security.auth.callback.Callback;

import okhttp3.Headers;


public class EdamamService {
    public final String TAG = "EdamamService";

    public String EDAMAM_ID = BuildConfig.EDAMAM_ID;
    public static final String EDAMAM_KEY = BuildConfig.EDAMAM_KEY;
    public static final String EDAMAM_BASE_URL = "https://api.edamam.com/api/food-database/v2/parser";

    public JSONObject findItemFromUPC(String query) {
        final JSONObject[] nutritionData = {new JSONObject()};
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(EDAMAM_BASE_URL + "?upc=" + query + "&app_id=" + EDAMAM_ID + "&app_key=" + EDAMAM_KEY, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                Log.d(TAG, jsonObject.toString());
                try {
                    JSONArray Array = jsonObject.getJSONArray("hints");
                    Log.i(TAG, "Food Array: " + Array.toString());
                    nutritionData[0] = Array.getJSONObject(0).getJSONObject("food").getJSONObject("nutrients");
                    Log.i(TAG, "Food Nutrition: " + nutritionData[0].toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        return nutritionData[0];
    }
}
