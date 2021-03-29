package com.groceryapp.upcdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.LoginStuff.LoginActivity;
import com.groceryapp.upcdata.fragments.BarcodeFragment;
import com.groceryapp.upcdata.fragments.GroceryListFragment;
import com.groceryapp.upcdata.fragments.InventoryFragment;
import com.groceryapp.upcdata.fragments.SettingsFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {



    // BOTTOM NAVIGATION:
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);

        // BOTTOM NAVIGATION:

        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new InventoryFragment();
                switch (item.getItemId()) {
                    case R.id.action_scanner:
                        fragment = new BarcodeFragment();
                        Toast.makeText(MainActivity.this, "Scanner!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_groceryList:
                        fragment = new GroceryListFragment();
                        Toast.makeText(MainActivity.this, "List!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_inventory:
                        fragment = new InventoryFragment();
                        Toast.makeText(MainActivity.this, "Inventory!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        fragment = new SettingsFragment();
                        Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }

        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_inventory);

        // END OF BOTTOM NAVIGATION SECTION


    }
}