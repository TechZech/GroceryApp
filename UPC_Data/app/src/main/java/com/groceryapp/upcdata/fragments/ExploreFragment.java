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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DB.UserGroupItem.UserGroupItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroupAdapter;
import com.groceryapp.upcdata.adapters.UserGroupItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExploreFragment extends Fragment {
    public final String TAG = "ExploreFragment";
    private static final int NUM_COLUMNS = 2;
    private DBHelper dbHelper = new DBHelper();
    private UserGroupItemAdapter.OnClickListener onClickListener;
    UserGroupItemAdapter staggeredRecyclerViewAdapter;
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<UserGroupItem> mUserGroupItems = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initImageBitmaps();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        onClickListener = new UserGroupItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {

            }
        };

        staggeredRecyclerViewAdapter = new UserGroupItemAdapter(getContext(), mUserGroupItems, onClickListener);
    }
    private void initImageBitmaps(){
        initRecyclerView();
        for(int kj = 0; kj<10; kj++){

            Random rand = new Random(); //instance of random class

            int postUserGroup = rand.nextInt(3);

            if(postUserGroup==0){ //get random post
                Random postRand = new Random(); //instance of random class
                Log.d("IN LOOP ", "KJ IS " +kj + " : " + postUserGroup);
            dbHelper.queryRandomPost(postRand.nextInt(2), mUserGroupItems, staggeredRecyclerViewAdapter, new DBHelper.UserGroupItemCallback() {
                @Override
                public void OnCallback(UserGroupItem userGroupItem) {

                    Log.d("IN CALLBACK", userGroupItem.getUgiGP().getUser().getUsername());
                    mUserGroupItems.add(userGroupItem);
                    staggeredRecyclerViewAdapter.notifyDataSetChanged();

                }
            });
                staggeredRecyclerViewAdapter.notifyDataSetChanged();
            }
            else if(postUserGroup==1){ //get random user
                Random userRand = new Random(); //instance of random class
                Log.d("IN LOOP ", "KJ IS " +kj + " : " + postUserGroup);
           dbHelper.queryRandomUser(userRand.nextInt(2), mUserGroupItems, staggeredRecyclerViewAdapter, new DBHelper.UserGroupItemCallback() {
               @Override
               public void OnCallback(UserGroupItem userGroupItem) {
                   Log.d("IN CALLBACK", userGroupItem.getUgiUser().getUsername());
                   mUserGroupItems.add(userGroupItem);
                   staggeredRecyclerViewAdapter.notifyDataSetChanged();

               }

           });
                staggeredRecyclerViewAdapter.notifyDataSetChanged();
            }
            else if(postUserGroup==2){ //get random group
                Random groupRand = new Random(); //instance of random class
                Log.d("IN LOOP ", "KJ IS " +kj + " : " + postUserGroup);
                dbHelper.queryRandomGroup(groupRand.nextInt(2), mUserGroupItems, staggeredRecyclerViewAdapter, new DBHelper.UserGroupItemCallback() {
                    @Override
                    public void OnCallback(UserGroupItem userGroupItem) {
                        Log.d("IN CALLBACK", userGroupItem.getUgiGroup().getGroupname());
                        mUserGroupItems.add(userGroupItem);
                        staggeredRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
                staggeredRecyclerViewAdapter.notifyDataSetChanged();
            }
            else{ // get random user just in case...
                Random userRand = new Random(); //instance of random class
                Log.d("IN LOOP ", "KJ IS " +kj + " : " + postUserGroup);
                dbHelper.queryRandomUser(userRand.nextInt(2), mUserGroupItems, staggeredRecyclerViewAdapter, new DBHelper.UserGroupItemCallback() {
                    @Override
                    public void OnCallback(UserGroupItem userGroupItem) {
                        Log.d("IN CALLBACK", userGroupItem.getUgiUser().getUsername());
                        mUserGroupItems.add(userGroupItem);
                        staggeredRecyclerViewAdapter.notifyDataSetChanged();

                    }
                });
staggeredRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");


        mImageUrls.add("https://s3.amazonaws.com/cdn-origin-etr.akc.org/wp-content/uploads/2017/11/14112506/Pembroke-Welsh-Corgi-standing-outdoors-in-the-fall.jpg");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");


        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");


        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");



    }


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: initializing staggered recyclerview.");

        RecyclerView recyclerView = getView().findViewById(R.id.stagRV);
        staggeredRecyclerViewAdapter =
                new UserGroupItemAdapter(getContext(), mUserGroupItems, onClickListener);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }
}



