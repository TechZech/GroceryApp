package com.groceryapp.upcdata.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.MainActivity;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;
import com.groceryapp.upcdata.map.MapActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostFragment extends DialogFragment {
    EditText itemTitleText;
    EditText  upctext;
    EditText urltext;
    EditText quantityText;
    EditText priceText;
    DBHelper dbhelper;
    Button saveButton;
    Scraper myScrap;
    TextView placeButton;
    Place postPlace;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User user = new User(mAuth);
    private PlacesClient placesClient;
    public static String TAG = "PostFragmentDialog";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    ArrayList<String> allBarcodeData;
    private void initServices(){

    }
    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: checking Google services version..." );
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if(available== ConnectionResult.SUCCESS){
            //everything is fine and user can make map requests
            Log.d(TAG, "isServicesOk: Google Play Services is working!");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(getContext(),"YOU CAN'T MAKE MAP REQUESTS....",Toast.LENGTH_SHORT);
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isServicesOk();
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemTitleText = view.findViewById(R.id.itemTitleText);
        myScrap = new Scraper();
        upctext = view.findViewById(R.id.upctext);
        urltext = view.findViewById(R.id.urltext);
        quantityText = view.findViewById(R.id.editquantity);
        placeButton = view.findViewById(R.id.placeButton);
        priceText = view.findViewById(R.id.priceText);
        //     searchRequestButton = view.findViewById(R.id.searchRequestButton); moved search to friends fragment

        dbhelper = new DBHelper();
        saveButton = view.findViewById(R.id.btnSaveChanges);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            allBarcodeData = myScrap.getAllData(upctext.getText().toString());
                            GroceryItem gi = new GroceryItem(allBarcodeData.get(1), upctext.getText().toString(), allBarcodeData.get(1), Integer.parseInt(quantityText.getText().toString()), allBarcodeData.get(3), true);
                       //     dbhelper.addInventoryItem(gi);
                            GroceryPost myGP = new GroceryPost();
                            myGP.setGroceryItem(gi);
                            myGP.setUser(user);
                            myGP.setPlaceid(postPlace.getId());
                            dbhelper.addPostItem(myGP); //if user setting for adding to feed automatically is off then you don't want to do this..
                            dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                            dismiss();
                        }
                    }
                });

                thread.start();




            }

        });
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivityForResult(intent,1000);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null){
            Log.d(TAG,"ACTIVITY CLOSED!!!!!!!!!");
            return;
        }


        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG,"ACTIVITY CLOSED!!!!!!!!!");
                Bundle Args = data.getExtras();
                // Define a Place ID.
                String placeid = Args.getString("placeid");
                Log.d(TAG, placeid);
                Places.initialize(getContext(), "AIzaSyBnC4uI9loLC3800-vS9IjmcVwG2jnjw2I");
                placesClient = Places.createClient(getContext());
                // Specify the fields to return.
                final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

            // Construct a request object, passing the place ID and fields array.
                final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeid, placeFields);
                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    postPlace = response.getPlace();

                    Log.i(TAG, "Place found: " + postPlace.getName());
                    placeButton.setText(postPlace.getName());
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
    }


}