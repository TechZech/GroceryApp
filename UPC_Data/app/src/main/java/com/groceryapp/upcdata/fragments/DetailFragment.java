package com.groceryapp.upcdata.fragments;

import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.NutritionData;
import com.groceryapp.upcdata.EdamamService;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.EditProfileFragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.grpc.NameResolver;

public class DetailFragment extends Fragment {

    public static final String TAG = "DetailFragment";
    GroceryItem groceryItem;

    ImageView ivDetailImage;
    TextView tvDetailTitle;
    TextView tvDetailUpc;
    TextView tvDetailQuantity;
    Button btnGoBack;
    Button btnNutrition;
    List<NutritionData> nutritionData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groceryItem = new GroceryItem();
        boolean fromInventory = unpackBundle();

        ivDetailImage = view.findViewById(R.id.ivDetailImage);
        ViewCompat.setTransitionName(ivDetailImage, "detail_item_image");
        tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
        tvDetailUpc = view.findViewById(R.id.tvDetailUpc);
        tvDetailQuantity = view.findViewById(R.id.tvDetailQuantity);
        btnGoBack = view.findViewById(R.id.btnGoBack);
        btnNutrition = view.findViewById(R.id.btnNutrition);

        Glide.with(getContext()).load(groceryItem.getImageUrl()).into(ivDetailImage);
        tvDetailTitle.setText(groceryItem.getTitle());
        tvDetailUpc.setText(groceryItem.getUpc());
        tvDetailQuantity.setText(String.valueOf(groceryItem.getQuantity()));

        nutritionData = new ArrayList<>();

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                if (fromInventory)
                    fragment = new InventoryFragment();
                else
                    fragment = new GroceryListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .replace(R.id.flContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btnNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNutritionData();
            }
        });
    }

    private boolean unpackBundle(){
        Bundle Args = getArguments();
        groceryItem.setUpc(Args.getString("UPC"));
        groceryItem.setTitle(Args.getString("Title"));
        groceryItem.setImageUrl(Args.getString("ImageUrl"));
        groceryItem.setQuantity(Args.getInt("Quantity"));
        return Args.getBoolean("fromInventory");
    }

    private void showNutritionData(){
        LayoutInflater factory = LayoutInflater.from(getActivity());

        EdamamService edamamService = new EdamamService();
        edamamService.findItemFromUPC("016000275287", nutritionData);
        final View View = factory.inflate(R.layout.nutrition_dialog, null);

        final TextView tvCalories = View.findViewById(R.id.calories);
        //tvCalories.setText(nutritionData.get(0).getCalories());

        final ImageView ivNutrition = (ImageView) View.findViewById(R.id.ivNutrition);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(View).setPositiveButton("EXIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {


                    }
                });
        alert.show();
    }
    }
