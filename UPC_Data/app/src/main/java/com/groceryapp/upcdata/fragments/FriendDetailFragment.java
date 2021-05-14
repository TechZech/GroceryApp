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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.FriendItemAdapter;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class FriendDetailFragment extends Fragment {

    public static final String TAG = "FriendDetailFragment";
    RecyclerView rvFriendInv;
    FriendItemAdapter friendItemAdapter;
    List<GroceryItem> allFriendsItems;
    ImageView ivFriendPic;
    TextView tvFriendName, tvFriendEmail;
    Button btnRemoveFriend, btnGroupInvite;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    User user = new User();
    DBHelper dbHelper = new DBHelper();
    User myUser = new User(mAuth);
Button plusButton;
Button minusButton;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private List<User> retttUser;

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
        final LayoutInflater factory = getLayoutInflater();

        final View itemGroceryView = factory.inflate(R.layout.item_grocery, null);
        plusButton = itemGroceryView.findViewById(R.id.ivPlusSign);
        minusButton = itemGroceryView.findViewById(R.id.ivMinusSign);

        unpackBundle();

        tvFriendName.setText(user.getUsername());
        tvFriendEmail.setText(user.getEmail());
        Glide.with(getContext()).load(user.getProfilePhotoURL()).into(ivFriendPic);

        btnRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.addFriend(user.getUserID());
            }
        });

        btnGroupInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Whatever code to invite this user to a group
            }
        });

        FriendItemAdapter.OnLongClickListener onLongClickListener1 = new FriendItemAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                GroceryItem groceryItem = allFriendsItems.get(position);
                Log.i(TAG, "The User long-clicked on " + groceryItem.getTitle() + " and nothing should happen to this item");
            }
        };

        FriendItemAdapter.OnClickListener onClickListener1 = new FriendItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                goToDetailFragment(position);
            }
        };
        allFriendsItems = new ArrayList<>();
        friendItemAdapter = new FriendItemAdapter(getContext(), allFriendsItems, onLongClickListener1, onClickListener1);

      //  groceryItemAdapter = new GroceryItemAdapter(getContext(), allFriendsItems, onLongClickListener, onClickListener, subtractListener);

        rvFriendInv.setAdapter(friendItemAdapter);
        rvFriendInv.setLayoutManager(linearLayoutManager);
        allFriendsItems = dbHelper.queryFriendInventoryItems(user.getUserID(), allFriendsItems, friendItemAdapter);
    }

    public void makePublic(){
        Log.d(TAG,"CALLING MAKE PUBLIC");
        rvFriendInv.setVisibility(View.VISIBLE);
        friendItemAdapter.notifyDataSetChanged();


    }
    public void makePrivate(){
        Log.d(TAG,"CALLING MAKE PRIVATE");
        rvFriendInv.setVisibility(GONE);
        btnGroupInvite.setVisibility(GONE);


    }
    private void unpackBundle(){
        Bundle Args = getArguments();
        user.setEmail(Args.getString("email"));
        user.setUserID(Args.getString("userID"));
        user.setUsername(Args.getString("username"));
        user.setProfilePhotoURL(Args.getString("photoUrl"));
        dbHelper.queryUserSetting("Visibility", user, new DBHelper.SettingCallback() {
            @Override
            public void OnCallback(Boolean value) {
                if(value==true){
                    Log.d(TAG,"SETTIGNS CALLBACK");
                    makePublic();
                }
                else{
                    Log.d(TAG,"SETTIGNS CALLBACK");
                    makePrivate();
                }
            }
        });
        dbHelper.areFriends(user.getUserID(), myUser.getUserID(), new DBHelper.AreFriendsCallback() {
            @Override
            public void OnCallBack(Boolean usersAreFriends) {
                if(usersAreFriends==Boolean.TRUE){
                    makePublic();
                    btnRemoveFriend.setText("Remove Friend");


                    btnRemoveFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbHelper.deleteFriend(user.getUserID());
                        }
                    });
                } //else users aren't friends...
            }
        });
         //users aren't friends so we need to take into account privacy settings.

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
