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

import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
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
    User us;
    DBHelper dbHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean inUserDetail = unpackBundle();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        dbHelper = new DBHelper();
        GroupAdapter.OnClickListener onClickListener = new GroupAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position)  {
                Log.d(TAG, "ITEM CLICKED HERE");
                if(inUserDetail){
                    dbHelper.inviteToGroup(allFriends.get(position), us);
                    Log.d(TAG, "INVITED TO GROUP");
                }
                else{
                    goToDetailFragment(position);
                }

            }
        };
        rvFriends = view.findViewById(R.id.rvSearch);
        frTV = view.findViewById(R.id.FRLabel);
        SearchText = view.findViewById(R.id.searchText1);
        allFriends  = new ArrayList<>();
        adapter = new GroupAdapter(onClickListener, getContext(), allFriends);

        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(linearLayoutManager);
        allFriends = dbHelper.getUserGroups(allFriends, adapter);

        Log.d(TAG, "ALL GROUPS SIZE IS" + allFriends.size());

    }

    private void goToDetailFragment(int position){
        Group gg = allFriends.get(position);
        Bundle bundle = new Bundle();
        String gidString = gg.getGid();
        bundle.putString("gid", gidString );
        bundle.putBoolean("fromInventory", true);
        Fragment fragment = new GroupDetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public boolean unpackBundle(){
        Bundle Args = getArguments();
        if(Args.getBoolean("fromProfile")){
            us = new User(Args.getString("userID"),Args.getString("username"), Args.getString("email") );
            return Args.getBoolean("fromProfile");
        }
        else{
        return false;
        }


    }
}
