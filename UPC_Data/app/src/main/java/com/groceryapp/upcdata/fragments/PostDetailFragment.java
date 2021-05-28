package com.groceryapp.upcdata.fragments;

import android.content.DialogInterface;
import android.graphics.Typeface;
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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.groceryapp.upcdata.BuildConfig;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.GroceryItem.NutritionData;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.EdamamService;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "DetailFragment";
    GroceryPost myGroceryPost = new GroceryPost();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    private static final int DEFAULT_ZOOM = 15;
    private PlacesClient placesClient;
    Place postPlace;
    ImageView ivDetailImage;
    CircleImageView ivUserPhoto;
    TextView titleText;
    TextView dt;
    EditText commentText;
    TextView tvDetailPrice;
    TextView tvDetailQuantity;
    Button btnGoBack;
    Button submitComment;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper();
        ivDetailImage = view.findViewById(R.id.imageView);
        titleText = view.findViewById(R.id.titleText);
        dt =  view.findViewById(R.id.dateTimeInfo);
        ivUserPhoto = view.findViewById(R.id.ivPostDetailUserPic);
        commentText = view.findViewById(R.id.commentText);
        submitComment = view.findViewById(R.id.button2);
   //     ViewCompat.setTransitionName(ivDetailImage, "detail_item_image");
    //    tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
     //   tvDetailUpc = view.findViewById(R.id.tvDetailUpc);
   //     tvDetailPrice = view.findViewById(R.id.tvDetailPrice);
   //     tvDetailQuantity = view.findViewById(R.id.tvDetailQuantity);

        btnGoBack = view.findViewById(R.id.btnGoBackFromFeedDetail);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dbHelper.addComment(myGroceryPost, commentText.getText().toString());
            }
        });
        unpackBundle();
      //
       // tvDetailTitle.setText(groceryItem.getTitle());
       // tvDetailUpc.setText(groceryItem.getUpc());
      //  tvDetailPrice.setText(groceryItem.getPrice());
      //  tvDetailQuantity.setText(String.valueOf(groceryItem.getQuantity()));

        ivDetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Detail photo clicked");
                goToDetailFragment();
            }
        });


    }

    private void unpackBundle(){
        Bundle Args = getArguments();
        myGroceryPost.setPid(Args.getString("Pid"));
        myGroceryPost.groceryItem.setTitle(Args.getString("Title"));
        titleText.setText(myGroceryPost.groceryItem.getTitle());
        myGroceryPost.groceryItem.setImageUrl(Args.getString("ImageUrl"));
        dt.setText(Args.getString("DateTime"));
        myGroceryPost.setPlaceid(Args.getString("placeid"));

        Glide.with(getContext()).load(myGroceryPost.groceryItem.getImageUrl()).into(ivDetailImage);
        Glide.with(getContext()).load(myGroceryPost.user.getProfilePhotoURL()).into(ivUserPhoto);
    }
    private void goToDetailFragment(){
        Bundle bundle = getArguments();
        Fragment fragment = new DetailFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Places.initialize(getContext(), BuildConfig.PLACES_KEY);
        placesClient = Places.createClient(getContext());
        // Specify the fields to return.
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(myGroceryPost.getPlaceid(), placeFields);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            postPlace = response.getPlace();
            SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(this);
            Log.i(TAG, "Place found: " + postPlace.getName());
            map.addMarker(new MarkerOptions()
                    .title(myGroceryPost.groceryItem.getTitle())
                    .position(postPlace.getLatLng()));
           // placeButton.setText(postPlace.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // TODO: Handle error with given status code.
            }
        });

    }

    }

