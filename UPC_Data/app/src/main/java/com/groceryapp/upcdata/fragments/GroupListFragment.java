package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.Friend.Friend;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {
    public final String TAG = "GroupListFragment";
    private RecyclerView rvFriends;
    private TextView frTV;
    private Button frCount;
    protected GroupAdapter adapter;
    protected List<Group> allFriends;
    TextView SearchText;
    Button rvButton;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        dbHelper = new DBHelper();

        rvFriends = view.findViewById(R.id.rvSearch);
        frTV = view.findViewById(R.id.FRLabel);
        SearchText = view.findViewById(R.id.searchText);
        allFriends  = new ArrayList<>();
        adapter = new GroupAdapter(getContext(), allFriends);

        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(linearLayoutManager);
        allFriends = dbHelper.getUserGroups(allFriends, adapter);

        Log.d(TAG, "ALL GROUPS SIZE IS" + allFriends.size());

    }
/*
    private void goToDetailFragment(int position){
        Friend friend = allFriends.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("email", friend.getemail());
        bundle.putString("userID", friend.getuserID());
        bundle.putString("username", friend.getusername());
        Fragment fragment = new GroupDetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/
}
