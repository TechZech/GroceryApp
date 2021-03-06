package com.groceryapp.upcdata.fragments;

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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.adapters.GroceryPostAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailFragment extends Fragment {

    public static final String TAG = "GroupDetailFragment";
    User user;
    Group grr;
    ImageView ivDetailImage;
    TextView tvDetailTitle;
    TextView tvDetailUpc;
Button addPostButton;
Button settingsButton;
Button memberListButton;
Button btnGoBack;
List<GroceryPost> allPostItems;
RecyclerView recyclerView;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DBHelper dbHelper;
    GroceryPostAdapter groceryPostAdapter;
    boolean isM = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        boolean userProfile = unpackBundle();
        recyclerView = view.findViewById(R.id.groupFeedRV);
        ivDetailImage = view.findViewById(R.id.ivDetailImage);
        addPostButton = view.findViewById(R.id.addPostButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        memberListButton = view.findViewById(R.id.membersListButton);
        btnGoBack = view.findViewById(R.id.btnGoBackFromGroup);

        tvDetailUpc = view.findViewById(R.id.tvDetailUpc);
        GroceryItem groceryItem=new GroceryItem();
        GroceryPost gp = new GroceryPost(groceryItem, User);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToSettingsFragment();
            }

        });
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.addGroupPost(grr, gp);
            }
        });


        tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
        ViewCompat.setTransitionName(ivDetailImage, "detail_item_image");

        //  Glide.with(getContext()).load(groceryItem.getImageUrl()).into(ivDetailImage);
     //   tvDetailTitle.setText(grr.getGroupname());
    //    tvDetailUpc.setText();
//        tvDetailPrice.setText(user.getEmail());

        GroceryPostAdapter.OnClickListener onClickListener = new GroceryPostAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position)  {
                Log.d(TAG, "ITEM CLICKED HERE");
                goToDetailFragment(position);

            }
        };
        allPostItems = new ArrayList<>();
        groceryPostAdapter = new GroceryPostAdapter(getContext(), allPostItems, onClickListener);
        recyclerView.setAdapter(groceryPostAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
    public void displayPublic(){
        settingsButton.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        addPostButton.setVisibility(View.VISIBLE);
//        memberListButton.setVisibility(View.VISIBLE);
    }

    public void displayPrivate(){
        settingsButton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        addPostButton.setVisibility(View.GONE);
    //    memberListButton.setVisibility(View.GONE);
    }
    public void goToMembers(){
        Group gg = grr;
        Bundle bundle = new Bundle();
        String gidString = gg.getGid();
        bundle.putString("gid", gidString );
        bundle.putBoolean("fromInventory", true);
        Fragment fragment = new GroupMemberListFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private boolean unpackBundle(){
        Bundle Args = getArguments();
        Log.d(TAG, "ARGS STRING IS " + Args.getString("gid"));

        dbHelper.getGroupById(Args.getString("gid"), new DBHelper.GroupCallback() {
            @Override
            public void OnCallback(Group g) {
                grr = g;
               tvDetailTitle.setText(g.getGroupname());
               tvDetailUpc.setText(g.getOwner().getUsername());
               if(g.getPhotoUrl()!=null){
                   Glide.with(getContext())
                           .load(g.getPhotoUrl())
                           .into(ivDetailImage);
               }
               dbHelper.isMember(User, grr, new DBHelper.isMemberCallback() {
                   @Override
                   public void OnCallback(Boolean b) {
                       displayPublic();
                       isM=true;
                       return;
                   }
               });
                dbHelper.querySetting("visibility", grr, new DBHelper.SettingCallback() {
                    @Override
                    public void OnCallback(Boolean value) {
                        if(value==true){
                            Log.d(TAG,"SETTING IS" + value);
                            displayPublic();
                        }
                        else if(value==false){
                            if(isM == true){
                                displayPublic();
                            }
                            else{
                                displayPrivate();
                            }
                        }
                    }
                });
                allPostItems = dbHelper.getGroupPosts(grr,allPostItems, groceryPostAdapter);
         //       tvDetailTitle.setText(Args.getString("gid"));
            }
        });
        {

        }
        //grr = dbHelper.getGroupById(Args.getString("gid"));
        Log.d(TAG, "TESTESTEST");
        return Args.getBoolean("fromSearch");
    }


    private void goToSettingsFragment(){
        Bundle bundle = new Bundle();
        String gidString = grr.getGid();
        bundle.putString("gid", gidString );
        bundle.putBoolean("fromInventory", true);
        Fragment fragment = new GroupSettingsFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void goToDetailFragment(int position){
        GroceryPost groceryPost = allPostItems.get(position);
        Bundle bundle = new Bundle();
        // String gidString = groceryPost.getGid();
        //   bundle.putString("gid", gidString );
        //  bundle.putBoolean("fromInventory", true);
        Fragment fragment = new PostDetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
