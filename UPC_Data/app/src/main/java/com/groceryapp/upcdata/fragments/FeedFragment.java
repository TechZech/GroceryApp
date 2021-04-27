package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryPostAdapter;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    public final String TAG = "FeedFragment";

    private RecyclerView rvFeed;
    private Button addB;
    protected GroceryPostAdapter adapter;
    protected List<GroceryPost> FeedItems;
    DBHelper dbHelper = new DBHelper();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvFeed = view.findViewById(R.id.rvFeed);
        FeedItems = new ArrayList<>();
        adapter = new GroceryPostAdapter(getContext(), FeedItems);
        addB = view.findViewById(R.id.acceptButton);

        rvFeed.setAdapter(adapter);
        rvFeed.setLayoutManager(linearLayoutManager);
        FeedItems = dbHelper.queryFeedItems(FeedItems, adapter);

    }
}
