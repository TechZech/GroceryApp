package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.util.Log;
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

import com.groceryapp.upcdata.DB.Friend.Friend;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.FriendListAdapter;
import com.groceryapp.upcdata.adapters.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    public final String TAG = "FriendsFragment";
    private RecyclerView rvFriends;
    private TextView frTV;
    private Button frCount;
    protected FriendListAdapter adapter;
    protected UserAdapter useradapter;
    protected List<Friend> allFriends;
    protected List<User> allUsers;
    TextView SearchText;
    DBHelper dbHelper;
    EditText rvSearchText;
    Button rvButton;
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
        FriendListAdapter.OnClickListener onClickListener = new FriendListAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                goToDetailFragment(position);
            }
        };
        UserAdapter.OnClickListener onClickListener1 = new UserAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                goToUserDetailFragment(position);
            }
        };
        rvFriends = view.findViewById(R.id.rvSearch);
        frTV = view.findViewById(R.id.FRLabel);
        rvSearchText = view.findViewById(R.id.searchText);
        frCount = view.findViewById(R.id.frCounter);
        allFriends  = new ArrayList<>();
        allUsers = new ArrayList<>();
        useradapter = new UserAdapter(getContext(), allUsers, onClickListener1);
        adapter = new FriendListAdapter(getContext(), allFriends, onClickListener);
        rvButton = view.findViewById(R.id.rvButton);
        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(linearLayoutManager);
        allFriends = dbHelper.queryFriends(allFriends, adapter);

        rvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                dbHelper.queryUserSearch(allSearches, adapter, rvSearchText.getText().toString(), new DBHelper.MyUserSearchCallback() {
                    @Override
                    public void onCallback(List<User> value) {
                        allSearches = value;

                        adapter.notifyDataSetChanged();
                    }
                });
*/
                dbHelper.queryUserSearch(allUsers, useradapter, rvSearchText.getText().toString(), new DBHelper.MyUserSearchCallback() {
                    @Override
                    public void onCallback(List<User> value) {
                        allUsers = value;
                        rvFriends.setAdapter(useradapter);
                        useradapter.notifyDataSetChanged();
                    }
                });
            }
        });

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
        Log.d(TAG, "ALL FRIENDS SIZE IS" + allFriends.size());
        if(allFriends.size()!=0){
            frCount.setVisibility(View.GONE);
            frTV.setVisibility(View.VISIBLE);
        }
        else{
            frCount.setVisibility(View.VISIBLE);
            frTV.setVisibility(View.GONE);

        }
    }

    private void goToDetailFragment(int position){
        Friend friend = allFriends.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("email", friend.getemail());
        bundle.putString("userID", friend.getuserID());
        bundle.putString("username", friend.getusername());
        bundle.putString("photoUrl", friend.getPhotoUrl());
        Fragment fragment = new FriendDetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void goToUserDetailFragment(int position){
        User use = allUsers.get(position);
        Log.d(TAG,"USER IS " + use.getUsername());
        Bundle bundle = new Bundle();
        bundle.putString("email", use.getEmail());
        bundle.putString("userID", use.getUserID());
        bundle.putString("username", use.getUsername());
        bundle.putString("photoUrl", use.getProfilePhotoURL());
        Fragment fragment = new FriendDetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
