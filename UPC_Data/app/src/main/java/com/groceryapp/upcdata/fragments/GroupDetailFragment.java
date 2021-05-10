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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;
import com.groceryapp.upcdata.adapters.GroceryPostAdapter;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.EditProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailFragment extends Fragment {

    public static final String TAG = "GroupDetailFragment";
    User user;
    Group grr;
    ImageView ivDetailImage;
    TextView tvDetailTitle;
Button addPostButton;
Button settingsButton;
List<GroceryPost> allPostItems;
RecyclerView recyclerView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DBHelper dbHelper;
    GroceryPostAdapter groceryPostAdapter;
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
        GroceryItem groceryItem=new GroceryItem();
        GroceryPost gp = new GroceryPost(groceryItem, User);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GroupSettingsFragment();
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


        allPostItems = new ArrayList<>();
        groceryPostAdapter = new GroceryPostAdapter(getContext(), allPostItems);
        recyclerView.setAdapter(groceryPostAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
    private boolean unpackBundle(){
        Bundle Args = getArguments();
        Log.d(TAG, "ARGS STRING IS " + Args.getString("gid"));

        dbHelper.getGroupById(Args.getString("gid"), new DBHelper.GroupCallback() {
            @Override
            public void OnCallback(Group g) {
                grr = g;
               tvDetailTitle.setText(g.getGroupname());
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




}
