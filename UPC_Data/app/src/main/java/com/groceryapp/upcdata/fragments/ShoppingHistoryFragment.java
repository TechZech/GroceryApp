package com.groceryapp.upcdata.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.ShoppingTrip.ShoppingTrip;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;
import com.groceryapp.upcdata.adapters.ShoppingTripAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ShoppingHistoryFragment extends Fragment {

    public final String TAG = "ShoppingHistoryFragment";

    private RecyclerView rvShoppingHistory;
    protected ShoppingTripAdapter adapter;
    protected List<ShoppingTrip> allShoppingTrips;
    DBHelper dbHelper = new DBHelper();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        ShoppingTripAdapter.OnClickListener onClickListener = new ShoppingTripAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Make this show Detailed View of Trip
            }
        };

        rvShoppingHistory = view.findViewById(R.id.rvShoppingHistory);
        allShoppingTrips = new ArrayList<>();

        adapter = new ShoppingTripAdapter(getContext(), allShoppingTrips, onClickListener);

        rvShoppingHistory.setAdapter(adapter);
        rvShoppingHistory.setLayoutManager(linearLayoutManager);


        allShoppingTrips = dbHelper.queryShoppingTrips(allShoppingTrips, adapter);

        Collections.sort(allShoppingTrips, Collections.reverseOrder());

    }


}