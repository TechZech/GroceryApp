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
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroupAdapter;
import com.groceryapp.upcdata.adapters.UserGroupItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DebugExploreFragment extends Fragment {
    public final String TAG = "DebugExploreFragment";
    private static final int NUM_COLUMNS = 2;

    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private UserGroupItemAdapter.OnClickListener onClickListener;
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
        GroupAdapter.OnClickListener onClickListener = new GroupAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d(TAG, "ITEM CLICKED HERE");

            }
        };
    }
    private void initImageBitmaps(){
        for(int kj = 0; kj<10; kj++){
            Random rand = new Random(); //instance of random class
            int postUserGroup = rand.nextInt(2);
            if(postUserGroup==0){ //get random post
                Random postRand = new Random(); //instance of random class
            }
            else if(postUserGroup==1){ //get random user
                Random userRand = new Random(); //instance of random class
            }
            else if(postUserGroup==2){ //get random group
                Random groupRand = new Random(); //instance of random class
            }
            else{ // get random user just in case...
                Random userRand = new Random(); //instance of random class
            }
        }
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");


        mImageUrls.add("https://images-na.ssl-images-amazon.com/images/I/51tBJlChWdL._SL150_.jpg");
        mNames.add("Doritos Nacho Cheese Flavored   Tortilla Chips, 9.75 Ounce");
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/groceryapp-e3b9f.appspot.com/o/l2jHqanv3IQx2xTQl32SiaPkOX12?alt=media&token=083a5457-59ca-4077-a1a0-33b8c1db8052");
        mNames.add("kaitlin4");

        mImageUrls.add("https://images-na.ssl-images-amazon.com/images/I/51U%2BajT-ejL._SL150_.jpg");
        mNames.add("Tim Tam Cookies Arnotts | Tim Tams Chocolate Biscuits | Made in Australia | Choose Your Flavor (2 Pack) (Double Coat)");


        mImageUrls.add("https://avatarfiles.alphacoders.com/822/82209.jpg");
        mNames.add("Veggie Lovers");

        mImageUrls.add("https://images-na.ssl-images-amazon.com/images/I/41Xyf7v33IL._SL150_.jpg");
        mNames.add("Veggie Cece's Co, Cauliflower Rice Organic, 16 Ounce");

        mImageUrls.add("data:image/png;base64,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");
        mNames.add("Publix Shoppers");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/groceryapp-e3b9f.appspot.com/o/j6MM2iZ1fqaggwlKY0r55mCftZ82?alt=media&token=79e1de39-60f1-4793-bbfc-7efa50a93761");
        mNames.add("Trevor");








        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/groceryapp-e3b9f.appspot.com/o/tDBFIbWh3GQafipn0BSR2v4tfX82?alt=media&token=97ebe442-ecd6-4713-876d-8bcb8bb88d49");
        mNames.add("BenZech");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/groceryapp-e3b9f.appspot.com/o/9ol85ZFV73MAfPwW0kaQuCataS42?alt=media&token=ffbec48f-42a0-4004-9326-9eb71792d0c8");
        mNames.add("Devin");

        initRecyclerView();

    }


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: initializing staggered recyclerview.");

        RecyclerView recyclerView = getView().findViewById(R.id.stagRV);
        UserGroupItemAdapter staggeredRecyclerViewAdapter =
                new UserGroupItemAdapter(getContext(), mNames, mImageUrls, onClickListener);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(staggeredRecyclerViewAdapter);
    }
}


