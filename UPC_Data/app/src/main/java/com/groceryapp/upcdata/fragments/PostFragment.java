package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;

import java.io.IOException;
import java.util.ArrayList;

public class PostFragment extends DialogFragment {
    EditText itemTitleText;
    EditText  upctext;
    EditText urltext;
    EditText quantityText;
    EditText priceText;
    DBHelper dbhelper;
    Button saveButton;
    Scraper myScrap;

    ArrayList<String> allBarcodeData;
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemTitleText = view.findViewById(R.id.itemTitleText);
        myScrap = new Scraper();
        upctext = view.findViewById(R.id.upctext);
        urltext = view.findViewById(R.id.urltext);
        quantityText = view.findViewById(R.id.editquantity);
        priceText = view.findViewById(R.id.priceText);
        //     searchRequestButton = view.findViewById(R.id.searchRequestButton); moved search to friends fragment

        dbhelper = new DBHelper();
        saveButton = view.findViewById(R.id.btnSaveChanges);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            allBarcodeData = myScrap.getAllData(upctext.getText().toString());
                            GroceryItem gi = new GroceryItem(allBarcodeData.get(1), upctext.getText().toString(), allBarcodeData.get(1), Integer.parseInt(quantityText.getText().toString()), allBarcodeData.get(3), true);
                            dbhelper.addInventoryItem(gi);
                            dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                            dismiss();
                        }
                    }
                });

                thread.start();


            }

        });

    }

    public static String TAG = "PostFragmentDialog";
}