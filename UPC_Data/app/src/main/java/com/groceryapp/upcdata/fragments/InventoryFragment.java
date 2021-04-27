package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;

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

        GroceryItemAdapter.OnClickListener onClickListener = new GroceryItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(getContext(), "OnCLick", Toast.LENGTH_SHORT).show();
                goToDetailFragment(position);
            }
        };

        rvInventory = view.findViewById(R.id.tvInventory);
        allInventoryItems = new ArrayList<>();
        adapter = new GroceryItemAdapter(getContext(), allInventoryItems, onLongClickListener, onClickListener);

        rvInventory.setAdapter(adapter);
        rvInventory.setLayoutManager(linearLayoutManager);

        allInventoryItems = dbHelper.queryInventoryItems(allInventoryItems, adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                GroceryItem groceryItem = allInventoryItems.get(viewHolder.getAdapterPosition());
                Log.d(TAG, "groceryItem UPC to be removed" + groceryItem.getUpc());
                dbHelper.removeInventoryItem(groceryItem);
                allInventoryItems.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                dbHelper.addGroceryItem(groceryItem);
            }
        }).attachToRecyclerView(rvInventory);
    }

    private void goToDetailFragment(int position){
        GroceryItem groceryItem = allInventoryItems.get(position);
        Toast.makeText(getContext(), "OnClick", Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("UPC", groceryItem.getUpc());
        bundle.putString("Title", groceryItem.getTitle());
        bundle.putString("ImageUrl", groceryItem.getImageUrl());
        bundle.putString("Price", groceryItem.getPrice());
        bundle.putInt("Quantity", groceryItem.getQuantity());
        bundle.putBoolean("fromInventory", true);
        Fragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}