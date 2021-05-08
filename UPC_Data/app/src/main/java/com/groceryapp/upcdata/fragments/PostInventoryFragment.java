package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostInventoryFragment extends InventoryFragment {
    public final String TAG = "InventoryFragment";

    private RecyclerView rvInventory;
    protected GroceryItemAdapter adapter;
    protected List<GroceryItem> allInventoryItems;
    private Button createGroupButton;
    DBHelper dbHelper = new DBHelper();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        createGroupButton = view.findViewById(R.id.createGroupButton);
        GroceryItemAdapter.OnLongClickListener onLongClickListener = new GroceryItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                GroceryItem groceryItem = allInventoryItems.get(position);
                Log.d(TAG, "groceryItem UPC to be removed" + groceryItem.getUpc());
                dbHelper.removeInventoryItem(groceryItem);
                allInventoryItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GroceryItem g = new GroceryItem("Test","12345","http:www.google.com/",5,"4.99",true);
                dbHelper.addInventoryItem(g);
            }
        });
        GroceryItemAdapter.OnClickListener onClickListener = new GroceryItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                    Log.d(TAG, "ITEM IS " + allInventoryItems.get(position));
                    goToDetailFragment(position);
            }
        };

        GroceryItemAdapter.OnClickListenerQuantitySubtract subtractListener = new GroceryItemAdapter.OnClickListenerQuantitySubtract() {

            @Override
            public void onSubtractClicked(int position) {
                GroceryItem groceryItem = allInventoryItems.get(position);
                groceryItem.setInventory(false);
                Log.d(TAG, "groceryItem UPC to be removed" + groceryItem.getUpc());
                dbHelper.removeInventoryItem(groceryItem);
                allInventoryItems.remove(position);
                adapter.notifyItemRemoved(position);
                dbHelper.addGroceryItem(groceryItem);
            }
        };

        rvInventory = view.findViewById(R.id.tvInventory);

        allInventoryItems = new ArrayList<>();

        adapter = new GroceryItemAdapter(getContext(), allInventoryItems, onLongClickListener, onClickListener, subtractListener);

        rvInventory.setAdapter(adapter);
        rvInventory.setLayoutManager(linearLayoutManager);

        allInventoryItems = dbHelper.queryInventoryItems(allInventoryItems, adapter);



    }
    private void goToDetailFragment(int position){


        GroceryItem groceryItem = allInventoryItems.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("UPC", groceryItem.getUpc());
        bundle.putString("Title", groceryItem.getTitle());
        bundle.putString("ImageUrl", groceryItem.getImageUrl());
        bundle.putString("Price", groceryItem.getPrice());
        bundle.putInt("Quantity", groceryItem.getQuantity());
        bundle.putBoolean("fromInventory", true);
        Fragment fragment = new FeedFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
               .replace(R.id.flContainer, fragment);
       fragmentTransaction.addToBackStack(null);
       fragmentTransaction.commit();
    }
}
