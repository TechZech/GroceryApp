package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroceryListFragment extends Fragment {

    public final String TAG = "GroceryListFragment";

    private RecyclerView rvGroceryItems;
    protected GroceryItemAdapter adapter;
    protected List<GroceryItem> allGroceryItems;
     DBHelper dbHelper = new DBHelper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grocerylist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        GroceryItemAdapter.OnLongClickListener onLongClickListener = new GroceryItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                GroceryItem groceryItem = allGroceryItems.get(position);
                dbHelper.removeGroceryItem(groceryItem);
                allGroceryItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        };

        GroceryItemAdapter.OnClickListener onClickListener = new GroceryItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                goToDetailFragment(position);
            }
        };

        rvGroceryItems = view.findViewById(R.id.tvGroceryItems);
        allGroceryItems = new ArrayList<>();
        adapter = new GroceryItemAdapter(getContext(), allGroceryItems, onLongClickListener, onClickListener);

        rvGroceryItems.setAdapter(adapter);
        rvGroceryItems.setLayoutManager(linearLayoutManager);

        allGroceryItems = dbHelper.queryGroceryItems(allGroceryItems, adapter);

        //EdamamService edamamService = new EdamamService();
        //edamamService.findItemFromUPC("016000275287");
    }

private void goToDetailFragment(int position){
    GroceryItem groceryItem = allGroceryItems.get(position);
    Bundle bundle = new Bundle();
    bundle.putString("UPC", groceryItem.getUpc());
    bundle.putString("Title", groceryItem.getTitle());
    bundle.putString("ImageUrl", groceryItem.getImageUrl());
    bundle.putString("Price", groceryItem.getPrice());
    bundle.putInt("Quantity", groceryItem.getQuantity());
    bundle.putBoolean("fromInventory", false);
    Fragment fragment = new DetailFragment();
    fragment.setArguments(bundle);
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.flContainer, fragment);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
    }
}