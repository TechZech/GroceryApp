package com.groceryapp.upcdata.fragments;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DB.GroceryItem.NutritionData;
import com.groceryapp.upcdata.EdamamService;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.EditProfileFragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import io.grpc.NameResolver;

public class userProfileFragment extends Fragment {

    public static final String TAG = "DetailFragment";
    User user;

    ImageView ivDetailImage;
    TextView tvDetailTitle;
    TextView tvDetailUpc;
    TextView tvDetailPrice;
    TextView tvDetailQuantity;
    Button btnGoBack;
    Button btnNutrition;
    Button btnSimilarProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_profile_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = new User();
        boolean userProfile = unpackBundle();

        ivDetailImage = view.findViewById(R.id.ivDetailImage);
        ViewCompat.setTransitionName(ivDetailImage, "detail_item_image");
        tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
        tvDetailUpc = view.findViewById(R.id.tvDetailUpc);
        tvDetailPrice = view.findViewById(R.id.tvDetailPrice);
        tvDetailQuantity = view.findViewById(R.id.tvDetailQuantity);
        btnGoBack = view.findViewById(R.id.btnGoBack);
        btnNutrition = view.findViewById(R.id.btnNutrition);
        btnSimilarProducts = view.findViewById(R.id.btnSimilarProducts);

      //  Glide.with(getContext()).load(groceryItem.getImageUrl()).into(ivDetailImage);
        tvDetailTitle.setText(user.getUsername());
        tvDetailUpc.setText(user.getUserID());
        tvDetailPrice.setText(user.getEmail());

    }
    private boolean unpackBundle(){
        Bundle Args = getArguments();
        user.setEmail(Args.getString("email"));
        user.setUserID(Args.getString("userID"));
        user.setUsername(Args.getString("username"));
        return Args.getBoolean("fromSearch");
    }




}
