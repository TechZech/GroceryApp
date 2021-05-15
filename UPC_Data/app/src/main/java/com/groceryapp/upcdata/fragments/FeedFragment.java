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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryPostAdapter;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.EditProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    public final String TAG = "FeedFragment";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);
    private RecyclerView rvFeed;
    private Button chooseItem;
    Button submitButton;
    TextView itemName;
    GroceryItem groceryItem = new GroceryItem();
    protected GroceryPostAdapter adapter;
    protected List<GroceryPost> FeedItems;
    DBHelper dbHelper = new DBHelper();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onResume() {

        super.onResume();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvFeed = view.findViewById(R.id.rvFeed);
        itemName = view.findViewById(R.id.itemName);
        submitButton = view.findViewById(R.id.submitButton);
        FeedItems = new ArrayList<>();
        adapter = new GroceryPostAdapter(getContext(), FeedItems);
        chooseItem = view.findViewById(R.id.chooseItemButton);

        chooseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // From another Fragment or Activity where you wish to show this
// PurchaseConfirmationDialogFragment.
                new PostFragment().show(
                        getChildFragmentManager(), PostFragment.TAG);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemName.getText().toString().equals("DEFAULT TEXT")) {

                }
                else{

                    GroceryItem gg = new GroceryItem(groceryItem.getTitle(),groceryItem.getUpc(),groceryItem.getImageUrl(),groceryItem.getQuantity(),groceryItem.getPrice(),groceryItem.isInventory() );
                    GroceryPost g = new GroceryPost(gg, User);
                    dbHelper.addPostItem(g);
                }
            }
        });
        rvFeed.setAdapter(adapter);
        rvFeed.setLayoutManager(linearLayoutManager);
        FeedItems = dbHelper.queryFriendFeedItems(FeedItems, adapter);
        Bundle b = this.getArguments();
        if(b!=null){
            unpackBundle();
            if(groceryItem.getTitle().equals("")){
                Log.d(TAG, "CHANGED TO DEFAULT TEXT");
                    itemName.setText("DEFAULT TEXT");
            }
            else{
                Log.d(TAG,"GROCERYYYYYYYYYYYYYYYYYYYYY " + groceryItem.getTitle());

                itemName.setText(groceryItem.getTitle());
            }

        }
        else{

            itemName.setText("DEFAULT TEXT");
        }

        Log.d(TAG, "GROCERY ITEM IS: " + groceryItem.getTitle());
    }

    private void unpackBundle(){
        Bundle Args = getArguments();
        groceryItem.setUpc(Args.getString("UPC"));
        groceryItem.setTitle(Args.getString("Title"));
        groceryItem.setImageUrl(Args.getString("ImageUrl"));
        groceryItem.setPrice(Args.getString("Price"));
        groceryItem.setQuantity(Args.getInt("Quantity"));
    }
}
