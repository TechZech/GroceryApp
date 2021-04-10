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
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.groceryapp.upcdata.Scraper;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.EditProfileFragment;

import java.io.IOException;

public class BarcodeFragment extends Fragment{

    Button scanBtn;
    DBHelper DB;
    Scraper scrap;
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

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){

            if(result.getContents() != null){
                scrap = new Scraper();
                DB = new DBHelper();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(result.getContents());
                GroceryItem groceryItem = new GroceryItem();
                groceryItem.setUpc(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Barcode Number for getContents: " + result.getContents());
                        Log.i(TAG, "Barcode Number for getUPC: " + groceryItem.getUpc());

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try  {
                                    Log.i(TAG, "Title: " + scrap.getUPCData(groceryItem.getUpc()));
                                    groceryItem.setTitle(scrap.getUPCData(groceryItem.getUpc()));
                                    DB.addGroceryItem(groceryItem);
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
}
