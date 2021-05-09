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

public class DetailFragment extends Fragment {

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
        tvDetailPrice = view.findViewById(R.id.tvDetailPrice);
        tvDetailQuantity = view.findViewById(R.id.tvDetailQuantity);
        btnGoBack = view.findViewById(R.id.btnGoBack);
        btnNutrition = view.findViewById(R.id.addButton);
        btnSimilarProducts = view.findViewById(R.id.deleteButton);

        Glide.with(getContext()).load(groceryItem.getImageUrl()).into(ivDetailImage);
        tvDetailTitle.setText(groceryItem.getTitle());
        tvDetailUpc.setText(groceryItem.getUpc());
        tvDetailPrice.setText(groceryItem.getPrice());
        tvDetailQuantity.setText(String.valueOf(groceryItem.getQuantity()));

        nutritionDataList = new ArrayList<>();
        EdamamService edamamService = new EdamamService();
        edamamService.findItemFromUPC(groceryItem.getUpc(), new EdamamService.EdamamCallback() {
            @Override
            public void onResponse(NutritionData nutritionData) {
                nutritionDataList.add(nutritionData);
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        btnNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNutritionData();
            }
        });

        btnSimilarProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Scraper scrap = new Scraper();
                            showSimilarProducts(scrap.getSimilarProducts(groceryItem.getUpc()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    });
                thread.start();
            }
        });
    }

    private boolean unpackBundle(){
        Bundle Args = getArguments();
        groceryItem.setUpc(Args.getString("UPC"));
        groceryItem.setTitle(Args.getString("Title"));
        groceryItem.setImageUrl(Args.getString("ImageUrl"));
        groceryItem.setPrice(Args.getString("Price"));
        groceryItem.setQuantity(Args.getInt("Quantity"));
        return Args.getBoolean("fromInventory");
    }

    private void showNutritionData() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        Typeface face = Typeface.createFromAsset(getContext().getResources().getAssets(), "helveticabold.ttf");
        final View View = factory.inflate(R.layout.nutrition_dialog, null);
        TextView tvCalories = View.findViewById(R.id.calories);
        TextView tvCalFromFat = View.findViewById(R.id.calFromFat);
        TextView tvFat = View.findViewById(R.id.fat);
        TextView tvSatFat = View.findViewById(R.id.fatsat);
        TextView tvTransFat = View.findViewById(R.id.fattrans);
        TextView tvChol = View.findViewById(R.id.cholestorol);
        TextView tvSodium = View.findViewById(R.id.sodium);
        TextView tvCarbs = View.findViewById(R.id.carbs);
        TextView tvFiber = View.findViewById(R.id.fiber);
        TextView tvSugars = View.findViewById(R.id.sugars);
        TextView tvProtein = View.findViewById(R.id.protein);

        tvCalories.setTypeface(face);
        tvFat.setTypeface(face);
        tvCalFromFat.setTypeface(face);

        if (nutritionDataList.size() != 0) {

            tvCalories.setText(nutritionDataList.get(0).getCalories());
            tvCalFromFat.setText(nutritionDataList.get(0).getCalFromFat());
            tvFat.setText(nutritionDataList.get(0).getTotalfat());
            tvSatFat.setText(nutritionDataList.get(0).getSatfat());
            tvTransFat.setText(nutritionDataList.get(0).getTransfat());
            tvChol.setText(nutritionDataList.get(0).getCholesterol());
            tvSodium.setText(nutritionDataList.get(0).getSodium());
            tvCarbs.setText(nutritionDataList.get(0).getCarbs());
            tvFiber.setText(nutritionDataList.get(0).getFiber());
            tvSugars.setText(nutritionDataList.get(0).getSugars());
            tvProtein.setText(nutritionDataList.get(0).getProtein());

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
        else{
            Toast.makeText(getContext(), "Oh No! Nutrtion Data isn't available for this item :(", Toast.LENGTH_LONG).show();
        }
    }

    private void showSimilarProducts(ArrayList<GroceryItem> products) throws IOException {
        Bundle bundle = new Bundle();
        bundle.putString("Picture1", products.get(0).getImageUrl());
        bundle.putString("Picture2", products.get(1).getImageUrl());
        bundle.putString("Picture3", products.get(2).getImageUrl());
        bundle.putString("Title1", products.get(0).getTitle());
        bundle.putString("Title2", products.get(1).getTitle());
        bundle.putString("Title3", products.get(2).getTitle());
        bundle.putString("UPC1", products.get(0).getUpc());
        bundle.putString("UPC2", products.get(1).getUpc());
        bundle.putString("UPC3", products.get(2).getUpc());
        Fragment fragment = new SimilarProductsFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void SetUpNutritionDialog(){


    }
    }
