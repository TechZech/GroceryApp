package com.groceryapp.upcdata.fragments;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.NutritionData;
import com.groceryapp.upcdata.EdamamService;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostDetailFragment extends Fragment {

    public static final String TAG = "DetailFragment";
    GroceryItem groceryItem;

    ImageView ivDetailImage;
    TextView tvDetailTitle;
    TextView tvDetailUpc;
    TextView tvDetailPrice;
    TextView tvDetailQuantity;
    Button btnGoBack;
    Button btnNutrition;
    Button btnSimilarProducts;
    List<NutritionData> nutritionDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groceryItem = new GroceryItem();
        unpackBundle();

        ivDetailImage = view.findViewById(R.id.ivDetailImage);
        ViewCompat.setTransitionName(ivDetailImage, "detail_item_image");
        tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
        tvDetailUpc = view.findViewById(R.id.tvDetailUpc);
        tvDetailPrice = view.findViewById(R.id.tvDetailPrice);
        tvDetailQuantity = view.findViewById(R.id.tvDetailQuantity);
        btnGoBack = view.findViewById(R.id.btnGoBack);
        btnNutrition = view.findViewById(R.id.membersListButton);
        btnSimilarProducts = view.findViewById(R.id.settingsButton);

        Glide.with(getContext()).load(groceryItem.getImageUrl()).into(ivDetailImage);
        tvDetailTitle.setText(groceryItem.getTitle());
        tvDetailUpc.setText(groceryItem.getUpc());
        tvDetailPrice.setText(groceryItem.getPrice());
        tvDetailQuantity.setText(String.valueOf(groceryItem.getQuantity()));


        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });



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
