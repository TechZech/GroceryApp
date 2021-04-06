package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
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
        EditText editTextName;
        Button btnClickHere;
        TextView textName;
        Scraper myScrap = new Scraper();
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
        btnClickHere.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String upccode = editTextName.getText().toString();
                //    textName.setText(name);
                try {
                    textName.setText(myScrap.getUPCData(upccode));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}