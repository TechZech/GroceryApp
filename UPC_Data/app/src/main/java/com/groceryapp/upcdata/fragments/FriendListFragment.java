package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.User.Friend;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.FriendListAdapter;
import com.groceryapp.upcdata.adapters.FriendRequestAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    public final String TAG = "FriendsFragment";
    private RecyclerView rvFriends;
    private TextView frTV;
    private Button frCount;
    protected FriendListAdapter adapter;
    protected List<Friend> allFriends;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        dbHelper = new DBHelper();
        rvFriends = view.findViewById(R.id.rvFriends);
        frTV = view.findViewById(R.id.FRLabel);
        frCount = view.findViewById(R.id.frCounter);
        allFriends  = new ArrayList<>();
        adapter = new FriendListAdapter(getContext(), allFriends);

        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(linearLayoutManager);
        allFriends = dbHelper.queryFriends(allFriends, adapter);
        frCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FriendRequestFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out
                        )
                        .replace(R.id.flContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        if(allFriends.size()==0){
            frCount.setVisibility(View.GONE);
        }
        else{
            frTV.setVisibility(View.GONE);
            frCount.setText(allFriends.size());
        }
    }
}
