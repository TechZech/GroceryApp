package com.groceryapp.upcdata.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.MainActivity;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.Scraper;
import com.groceryapp.upcdata.map.MapActivity;

import java.io.IOException;
import java.util.ArrayList;

public class PostFragment extends DialogFragment {
    EditText itemTitleText;
    EditText  upctext;
    EditText urltext;
    EditText quantityText;
    EditText priceText;
    DBHelper dbhelper;
    Button saveButton;
    Scraper myScrap;
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
        priceText = view.findViewById(R.id.priceText);
        //     searchRequestButton = view.findViewById(R.id.searchRequestButton); moved search to friends fragment

        dbhelper = new DBHelper();
        saveButton = view.findViewById(R.id.btnSaveChanges);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            allBarcodeData = myScrap.getAllData(upctext.getText().toString());
                            GroceryItem gi = new GroceryItem(allBarcodeData.get(1), upctext.getText().toString(), allBarcodeData.get(1), Integer.parseInt(quantityText.getText().toString()), allBarcodeData.get(3), true);
                            dbhelper.addInventoryItem(gi);
                            dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                            dismiss();
                        }
                    }
                });

                thread.start();
*/
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }

        });

    }

    public static String TAG = "PostFragmentDialog";
}