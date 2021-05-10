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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroupAdapter;
import com.groceryapp.upcdata.adapters.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    public final String TAG = "FriendsFragment";
    private RecyclerView rvSearch;
    protected UserAdapter adapter;
    protected GroupAdapter adapter2;
    protected List<User> allSearches;
    protected List<Group> allSearches2;
    Button rvButton;
    EditText rvSearchText;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
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
        GroupAdapter.OnClickListener onClickListener2 = new GroupAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
               goToDetailFragment(position);
            }
        };
        rvSearch = view.findViewById(R.id.rvSearch);
        rvButton = view.findViewById(R.id.rvButton);
        rvSearchText = view.findViewById(R.id.searchText);
        allSearches = new ArrayList<>();
        allSearches2 = new ArrayList<>();
        adapter = new UserAdapter(getContext(), allSearches, onClickListener);
        adapter2 = new GroupAdapter(onClickListener2, getContext(), allSearches2);
        rvSearch.setAdapter(adapter2);
        rvSearch.setLayoutManager(linearLayoutManager);
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
                dbHelper.queryGroupSearch(allSearches2, adapter2, rvSearchText.getText().toString(), new DBHelper.GroupSearchCallback() {
                    @Override
                    public void OnCallback(List<Group> value) {
                        allSearches2 = value;
                        adapter2.notifyDataSetChanged();
                    }
                });
            }
        });


    }
    /*
    private void goToDetailFragment(int position){
        User user = allSearches.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("email", user.getEmail());
        bundle.putString("userID", user.getUserID());
        bundle.putString("username", user.getUsername());
        Fragment fragment = new AddFriendDetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/

    private void goToDetailFragment(int position){
        Group gg = allSearches2.get(position);
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
}
