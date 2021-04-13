package com.groceryapp.upcdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    public final String TAG = "DBHelper";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public DBHelper(){
    }

    public List<GroceryItem> queryGroceryItems(List<GroceryItem> allGroceryItems, GroceryItemAdapter adapter){
        firestore.collection("users")
                .document(User.getUserID()).collection("Grocery List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                allGroceryItems.add(document.toObject(GroceryItem.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return allGroceryItems;
    }

    public List<GroceryItem> queryInventoryItems(List<GroceryItem> allInventoryItems, GroceryItemAdapter adapter){
        firestore.collection("users")
                .document(User.getUserID()).collection("Inventory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                allInventoryItems.add(document.toObject(GroceryItem.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        return allInventoryItems;
    }


    public void addGroceryItem(String itemName, String UPC){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List").document(UPC)
                .set(new GroceryItem(itemName, UPC));
    }

    public void addGroceryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List").document(groceryItem.getUpc())
                .set(groceryItem);
    }

    public void addInventoryItem(String itemName, String UPC){
        firestore.collection("users").document(User.getUserID()).collection("Inventory").document(UPC)
                .set(new GroceryItem(itemName, UPC));
    }

    public void addInventoryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Inventory").document(groceryItem.getUpc())
                .set(groceryItem);
    }

    public void removeGroceryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List")
                .document(groceryItem.getUpc()).delete();
    }

    public void removeInventoryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Inventory")
                .document(groceryItem.getUpc()).delete();
    }
}
