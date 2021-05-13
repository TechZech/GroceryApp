package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;

import java.util.ArrayList;

public class SimilarProductsFragment extends Fragment {

    public static final String TAG = "SimilarProductsFragment";
    ImageView ivProduct1, ivProduct2, ivProduct3;
    TextView tvTitle1, tvTitle2, tvTitle3;
    TextView tvUPC1, tvUPC2, tvUPC3;
    Button btnAdd1, btnAdd2, btnAdd3, btnGoBackToDetail;
    String scraperTitle, scraperImage, scraperPrice;
    ArrayList<String> allBarcodeData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.similar_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProduct1 = view.findViewById(R.id.ivProduct1);
        ivProduct2 = view.findViewById(R.id.ivProduct2);
        ivProduct3 = view.findViewById(R.id.ivProduct3);
        tvTitle1 = view.findViewById(R.id.tvTitle1);
        tvTitle2 = view.findViewById(R.id.tvTitle2);
        tvTitle3 = view.findViewById(R.id.tvTitle3);
        tvUPC1 = view.findViewById(R.id.tvUPC1);
        tvUPC2 = view.findViewById(R.id.tvUPC2);
        tvUPC3 = view.findViewById(R.id.tvUPC3);
        btnAdd1 = view.findViewById(R.id.btnAdd1);
        btnAdd2 = view.findViewById(R.id.btnAdd2);
        btnAdd3 = view.findViewById(R.id.btnAdd3);
        btnGoBackToDetail = view.findViewById(R.id.btnGoBackToDetail);
        unpackBundle();

        btnGoBackToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroceryItem groceryItem = new GroceryItem();
                groceryItem.setUpc((String) tvUPC1.getText());
                Log.i(TAG, "Add " + groceryItem.getUpc() + " to GList");
                addToGList(groceryItem);
            }
        });

        btnAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroceryItem groceryItem = new GroceryItem();
                groceryItem.setUpc((String) tvUPC2.getText());
                Log.i(TAG, "Add " + groceryItem.getUpc() + " to GList");
                addToGList(groceryItem);
            }
        });

        btnAdd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroceryItem groceryItem = new GroceryItem();
                groceryItem.setUpc((String) tvUPC3.getText());
                Log.i(TAG, "Add " + groceryItem.getUpc() + " to GList");
                addToGList(groceryItem);
            }
        });

    }

    private void unpackBundle(){
        Bundle ArgData = getArguments();
        Glide.with(getContext()).load(ArgData.getString("Picture1")).into(ivProduct1);
        Glide.with(getContext()).load(ArgData.getString("Picture2")).into(ivProduct2);
        Glide.with(getContext()).load(ArgData.getString("Picture3")).into(ivProduct3);
        tvTitle1.setText(ArgData.getString("Title1"));
        tvTitle2.setText(ArgData.getString("Title2"));
        tvTitle3.setText(ArgData.getString("Title3"));
        tvUPC1.setText("UPC: " + ArgData.getString("UPC1"));
        tvUPC2.setText("UPC: " + ArgData.getString("UPC2"));
        tvUPC3.setText("UPC: " + ArgData.getString("UPC3"));
    }

    private void addToGList(GroceryItem groceryItem){
        Scraper scrap = new Scraper();
        DBHelper DB = new DBHelper();
        allBarcodeData.clear();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    allBarcodeData = scrap.getAllData(groceryItem.getUpc());
                    scraperTitle = allBarcodeData.get(1);
                    scraperImage = allBarcodeData.get(2);
                    scraperPrice = allBarcodeData.get(3);
                    Log.i(TAG, "Title: " + scraperTitle);
                    Log.i(TAG, "ImageUrl: " + scraperImage);
                    Log.i(TAG, "Price: " + scraperPrice);
                    groceryItem.setTitle(scraperTitle);
                    groceryItem.setImageUrl(scraperImage);
                    groceryItem.setPrice(scraperPrice);
                    groceryItem.setQuantity(1);
                    DB.addGroceryItem(groceryItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
