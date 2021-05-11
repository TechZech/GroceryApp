package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.Friend.Friend;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.FriendRequestAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestFragment extends Fragment {
    public final String TAG = "FriendsFragment";
    private RecyclerView rvFriends;
    protected FriendRequestAdapter adapter;
    protected List<Friend> allFriendRequests;
    Button addFriendButton;
    EditText friendName;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friendrequests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        friendName = view.findViewById(R.id.searchText1);
        dbHelper = new DBHelper();


        rvFriends = view.findViewById(R.id.rvSearch);
        allFriendRequests = new ArrayList<>();
        adapter = new FriendRequestAdapter(getContext(), allFriendRequests);

        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(linearLayoutManager);
        allFriendRequests = dbHelper.queryFriendRequests(allFriendRequests, adapter);
    }
}
