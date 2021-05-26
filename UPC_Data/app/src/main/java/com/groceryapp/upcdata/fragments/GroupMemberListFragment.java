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
import com.groceryapp.upcdata.adapters.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberListFragment extends Fragment {
    public final String TAG = "GroupMemberListFragment";
    private RecyclerView rvFriends;
    private TextView frTV;
    private Button frCount;
    protected UserAdapter adapter;
    protected List<User> allFriends;
    Group grr;
    TextView SearchText;
    DBHelper dbHelper;
    Button btnGoBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_members, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        dbHelper = new DBHelper();
        UserAdapter.OnClickListener onClickListener = new UserAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                goToDetailFragment(position);
            }
        };
        boolean bund = unpackBundle();
        rvFriends = view.findViewById(R.id.staggeredRV);
        frTV = view.findViewById(R.id.FRLabel);
        btnGoBack = view.findViewById(R.id.btnGoBackFromGroupMembers);
        allFriends = new ArrayList<>();
        adapter = new UserAdapter(getContext(), allFriends, onClickListener);
        rvFriends.setAdapter(adapter);
        rvFriends.setLayoutManager(linearLayoutManager);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

    }
    private void goToDetailFragment(int position){
        User user = allFriends.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("email", user.getEmail() );
        bundle.putString("userID", user.getUserID());
        bundle.putString("username", user.getUsername());
        Fragment fragment = new FriendDetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public boolean unpackBundle(){
        Bundle Args = getArguments();
        Log.d(TAG, "GROUP IS " + Args.getString("gid"));
        dbHelper.getGroupById(Args.getString("gid"), new DBHelper.GroupCallback() {
            @Override
            public void OnCallback(Group g) {
                grr = g;

                dbHelper.queryGroupMembers(allFriends, g, adapter, new DBHelper.MemberListCallback() {
                    @Override
                    public void OnCallback(List<User> b) {
                        allFriends = b;
                        for(int ij = 0; ij<allFriends.size(); ij++){
                            Log.d(TAG,"ALL FRIENDS IS " + allFriends.get(ij).getUsername());
                        }


                    }
                });
            }

        });
        return Args.getBoolean("fromInventory");
    }
    }

