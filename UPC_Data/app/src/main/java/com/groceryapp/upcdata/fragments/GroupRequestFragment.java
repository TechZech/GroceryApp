package com.groceryapp.upcdata.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.Friend.Friend;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.FriendRequestAdapter;
import com.groceryapp.upcdata.adapters.GroupRequestAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupRequestFragment extends Fragment {
    public final String TAG = "FriendsFragment";
    private RecyclerView rvFriends;
    protected GroupRequestAdapter adapter;
    protected List<Group> allGroupRequests;
    Button addFriendButton;
    Button btnGoBack;
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
        btnGoBack = view.findViewById(R.id.btnGoBackFromFriendRequests);
        dbHelper = new DBHelper();

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        rvFriends = view.findViewById(R.id.staggeredRV);
        allGroupRequests = new ArrayList<>();
        adapter = new GroupRequestAdapter(getContext(), allGroupRequests);

        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(linearLayoutManager);
        allGroupRequests = dbHelper.queryGroupRequests(allGroupRequests, adapter);

    }

}
