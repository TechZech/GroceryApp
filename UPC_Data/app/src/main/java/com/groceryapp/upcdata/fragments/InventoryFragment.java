package com.groceryapp.upcdata.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.MainActivity;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;

import java.io.IOException;

public class InventoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DBHelper DB = new DBHelper(getContext());
        EditText editTextName;
        Button btnClickHere = view.findViewById(R.id.btnView);
        TextView textName;
        Scraper myScrap = new Scraper();
        GroceryItem groceryItem = new GroceryItem();
        groceryItem.setTitle("Test");
        groceryItem.setUpc("1234");
        DB.insertGroceryItemData(groceryItem);
        btnClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = DB.getdata();
                if(res.getCount()==0){
                    Toast.makeText(getContext(), "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Name :"+res.getString(0)+"\n");
                    buffer.append("Count :"+res.getString(1)+"\n");
                    buffer.append("UPC :"+res.getString(2)+"\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }        });
     //   DBHelper mydb = new DBHelper(getActivity());
       // User test = new User();

//        mydb.addOne(test);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        WebView wb = (WebView) view.findViewById(R.id.webview);
        wb.loadUrl("file:///android_asset/index.html");
        editTextName = (EditText) view.findViewById(R.id.upcData);
        btnClickHere = (Button) view.findViewById(R.id.sub);
         textName = (TextView) view.findViewById(R.id.retData);
        //btnClickHere.setVisibility(View.INVISIBLE);

    }
}