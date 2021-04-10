package com.groceryapp.upcdata.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.MainActivity;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    public final String TAG = "InventoryFragment";

    private RecyclerView rvInventory;
    protected GroceryItemAdapter adapter;
    protected List<GroceryItem> allInventoryItems;
    DBHelper dbHelper = new DBHelper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvInventory = view.findViewById(R.id.rvInventory);
        allInventoryItems = new ArrayList<>();
        adapter = new GroceryItemAdapter(getContext(), allInventoryItems);

        rvInventory.setAdapter(adapter);
        rvInventory.setLayoutManager(linearLayoutManager);

        allInventoryItems = dbHelper.queryInventoryItems(allInventoryItems, adapter);
    }

}