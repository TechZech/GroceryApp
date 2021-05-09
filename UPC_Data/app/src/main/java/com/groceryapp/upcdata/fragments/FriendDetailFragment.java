package com.groceryapp.upcdata.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendDetailFragment extends Fragment {

    public static final String TAG = "FriendDetailFragment";
    RecyclerView rvFriendInv;
    GroceryItemAdapter groceryItemAdapter;
    List<GroceryItem> allFriendsItems;
    ImageView ivFriendPic;
    TextView tvFriendName, tvFriendEmail;
    Button btnRemoveFriend, btnGroupInvite;
    User user = new User();
    DBHelper dbHelper = new DBHelper();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ivFriendPic = view.findViewById(R.id.ivFriendPic);
        tvFriendEmail = view.findViewById(R.id.tvFriendEmail);
        tvFriendName = view.findViewById(R.id.tvFriendName);
        btnRemoveFriend = view.findViewById(R.id.btnRemoveFriend);
        btnGroupInvite = view.findViewById(R.id.btnGroupInvite);
        rvFriendInv = view.findViewById(R.id.rvFriendItems);
        unpackBundle();

        tvFriendName.setText(user.getUsername());
        tvFriendEmail.setText(user.getEmail());

        btnRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteFriend(user.getUserID());
            }
        });

        btnGroupInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Whatever code to invite this user to a group
            }
        });

        GroceryItemAdapter.OnLongClickListener onLongClickListener = new GroceryItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                GroceryItem groceryItem = allFriendsItems.get(position);
                Log.i(TAG, "The User long-clicked on " + groceryItem.getTitle() + " and nothing should happen to this item");
            }
        };

        GroceryItemAdapter.OnClickListener onClickListener = new GroceryItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                goToDetailFragment(position);
            }
        };

        GroceryItemAdapter.OnClickListenerQuantitySubtract subtractListener = new GroceryItemAdapter.OnClickListenerQuantitySubtract(){
            @Override
            public void onSubtractClicked(int position) {
                GroceryItem groceryItem = allFriendsItems.get(position);
                Log.i(TAG, "The User clicked on " + groceryItem.getTitle() + " quantity button and nothing should happen to this item");
            }
        };

        allFriendsItems = new ArrayList<>();
        groceryItemAdapter = new GroceryItemAdapter(getContext(), allFriendsItems, onLongClickListener, onClickListener, subtractListener);
        rvFriendInv.setAdapter(groceryItemAdapter);
        rvFriendInv.setLayoutManager(linearLayoutManager);
        allFriendsItems = dbHelper.queryFriendInventoryItems(user.getUserID(), allFriendsItems, groceryItemAdapter);
    }

    private void unpackBundle(){
        Bundle Args = getArguments();
        user.setEmail(Args.getString("email"));
        user.setUserID(Args.getString("userID"));
        user.setUsername(Args.getString("username"));
    }

    private void goToDetailFragment(int position){
        GroceryItem groceryItem = allFriendsItems.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("UPC", groceryItem.getUpc());
        bundle.putString("Title", groceryItem.getTitle());
        bundle.putString("ImageUrl", groceryItem.getImageUrl());
        bundle.putString("Price", groceryItem.getPrice());
        bundle.putInt("Quantity", groceryItem.getQuantity());
        Fragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
