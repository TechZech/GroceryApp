package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.groceryapp.upcdata.BarcodeCamera;
import com.groceryapp.upcdata.CaptureAct;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.MainActivity;
import com.groceryapp.upcdata.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.groceryapp.upcdata.Scraper;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.EditProfileFragment;

import java.io.IOException;
import java.util.ArrayList;

public class BarcodeFragment extends Fragment{

    Button scanBtn;
    Button btnManualAdd;
    DBHelper DB;
    Scraper scrap;
    String productDetails = "default";
    String scraperTitle;
    String scraperImage;
    ArrayList<String> allBarcodeData = new ArrayList<>();
    private String TAG = "BarcodeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scanBtn = view.findViewById(R.id.scanBtn);
        btnManualAdd = view.findViewById(R.id.btnManualAdd);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        btnManualAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManualAdd();
            }
        });
    }

    private void scanCode() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(BarcodeFragment.this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    private void setProductDetails(String setThis){
        productDetails = setThis;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){

            if(result.getContents() != null){
                scrap = new Scraper();
                DB = new DBHelper();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                GroceryItem groceryItem = new GroceryItem();
                groceryItem.setUpc(result.getContents());
                /*Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            productDetails = scrap.getUPCData(groceryItem.getUpc());
                            setProductDetails(productDetails);
                        } catch (Exception e) {
                            e.printStackTrace();
                            productDetails = "No Product Found";
                            setProductDetails(productDetails);
                        }
                    }
                });
                thread.start();
                */
                builder.setMessage("Barcode Scanned: " + groceryItem.getUpc() + "\n\nIs This Correct?");
                builder.setTitle("Your Barcode Has Been Scanned!");
                builder.setPositiveButton("No, Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Yes, Add To Inventory", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Barcode Number for getContents: " + result.getContents());
                        Log.i(TAG, "Barcode Number for getUPC: " + groceryItem.getUpc());

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try  {
                                    Log.i(TAG, "PRICE: " + scrap.getPriceData(groceryItem.getUpc()));
                                    allBarcodeData = scrap.getAllData(groceryItem.getUpc());
                                    scraperTitle = allBarcodeData.get(1);
                                    scraperImage = allBarcodeData.get(2);
                                    Log.i(TAG, "Title: " + scraperTitle);
                                    Log.i(TAG, "ImageUrl: " + scraperImage);
                                    groceryItem.setTitle(scraperTitle);
                                    groceryItem.setImageUrl(scraperImage);
                                    DB.addInventoryItem(groceryItem);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        thread.start();

                        // sends user back to home screen
                        Fragment fragment = new GroceryListFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.flContainer, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Toast.makeText(getContext(), "No Results Found", Toast.LENGTH_LONG).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void ManualAdd(){
        LayoutInflater factory = LayoutInflater.from(getContext());

//text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.manual_add_dialog, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.input1);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.input2);
        final EditText input3 = (EditText) textEntryView.findViewById(R.id.input3);
        final EditText input4 = (EditText) textEntryView.findViewById(R.id.input4);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Manual Add").setView(textEntryView).setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        Log.i("AlertDialog","TextEntry 1 Entered "+input1.getText().toString());
                        Log.i("AlertDialog","TextEntry 2 Entered "+input2.getText().toString());
                        Log.i("AlertDialog","TextEntry 3 Entered "+input3.getText().toString());
                        Log.i("AlertDialog","TextEntry 4 Entered "+input4.getText().toString());
                        /* User clicked OK so do some stuff */
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        /*
                         * User clicked cancel so do some stuff
                         */
                    }
                });
        alert.show();
    }
}
