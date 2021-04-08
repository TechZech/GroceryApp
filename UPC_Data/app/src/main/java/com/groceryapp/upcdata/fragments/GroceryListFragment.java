package com.groceryapp.upcdata.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroceryListFragment extends Fragment {

    public final String TAG = "GroceryListFragment";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);
    private RecyclerView rvGroceryItems;
    protected GroceryItemAdapter adapter;
    protected List<GroceryItem> allGroceryItems;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grocerylist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rvGroceryItems = view.findViewById(R.id.rvGroceryItems);
        allGroceryItems = new ArrayList<>();
        adapter = new GroceryItemAdapter(getContext(), allGroceryItems);
        rvGroceryItems.setAdapter(adapter);
        rvGroceryItems.setLayoutManager(linearLayoutManager);

        queryGroceryItems();
        addGroceryItem("test", "test","test");
        adapter.notifyDataSetChanged();
    }

    private void queryGroceryItems(){
        List<GroceryItem> newGroceryItems = new ArrayList<>();
        firestore.collection("users")
                .document(User.getUserID()).collection("Grocery List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                newGroceryItems.add(document.toObject(GroceryItem.class));
                            }
                            allGroceryItems.addAll(newGroceryItems);
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "GroceryList Size is " + allGroceryItems.size());
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

    }

    private void addGroceryItem(String id, String itemName, String UPC){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List")
                .add(new GroceryItem(id, itemName, UPC));
    }
}