package com.groceryapp.upcdata;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.User.Friend;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.adapters.FriendListAdapter;
import com.groceryapp.upcdata.adapters.FriendRequestAdapter;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;
import com.groceryapp.upcdata.adapters.GroceryPostAdapter;
import com.groceryapp.upcdata.adapters.UserAdapter;

import java.util.List;

public class DBHelper {
    public final String TAG = "DBHelper";
    private String returnusername;
    private String returnemail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public DBHelper(){
    }
    public interface MyCallback {
        void onCallback(String value);
    }
    public interface MyUserSearchCallback {
        void onCallback(List<com.groceryapp.upcdata.DB.User.User> value);
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

    public List<GroceryPost> queryAllFeedItems(List<GroceryPost> FeedItems, GroceryPostAdapter adapter){
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=> " + document.getData());
                        FeedItems.add(document.toObject(GroceryPost.class));
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                    Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return FeedItems;
    }
    public boolean areFriends(String FriendAUid, String FriendBUid){
        Boolean ret = Boolean.FALSE;
        Log.d(TAG, "AREFRIENDS");
        firestore.collection("Friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=> " + document.getData());
                        if((Boolean) document.get(FriendBUid)){
                            Log.d(TAG, "AREFRIENDS");

                        }
                        else{
                            Log.d(TAG, "ARENNOTFRIEND");
                        }
                    }
                }
                else
                    Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return ret;
    }
    /*
    public GroceryItem createGroceryItemFromFirebase(DocumentSnapshot document){

        return document.getData();
    }

*/

    public List<GroceryPost> queryFriendFeedItems(List<GroceryPost> FeedItems, GroceryPostAdapter adapter){
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=> " + document.getData());
                        GroceryPost groceryPost = document.toObject(GroceryPost.class);
                        if(areFriends(User.getUserID(), groceryPost.getUid())) {
                            FeedItems.add(document.toObject(GroceryPost.class));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                else
                    Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return FeedItems;
    }
    public void addPostItem(GroceryPost groceryPost){
        firestore.collection("Posts").document().set(groceryPost);
    }

    public void addGroceryItem(String itemName, String UPC, String url, int quantity, String price, boolean isInventory){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List").document(UPC)
                .set(new GroceryItem(itemName, UPC, url, quantity, price, isInventory));
    }

    public void addGroceryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List").document(groceryItem.getUpc())
                .set(groceryItem);
    }

    public void addInventoryItem(String itemName, String UPC, String url, int quantity, String price, boolean isInventory){
        firestore.collection("users").document(User.getUserID()).collection("Inventory").document(UPC)
                .set(new GroceryItem(itemName, UPC, url, quantity, price, isInventory));

        firestore.collection("Posts").document().set(new GroceryPost(User.getUsername(), new GroceryItem(itemName, UPC, url, quantity, price, isInventory), true, User.getUserID() ));
    }

    public void addInventoryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Inventory").document(groceryItem.getUpc())
                .set(groceryItem);

        firestore.collection("Posts").document().set(new GroceryPost(User.getUsername(), groceryItem, true, User.getUserID() ));
    }

    public void removeGroceryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List")
                .document(groceryItem.getUpc()).delete();
    }

    public void removeInventoryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Inventory")
                .document(groceryItem.getUpc()).delete();
    }

    public void UpdateInventoryQuantity(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Inventory")
                .document(groceryItem.getUpc()).update("quantity", groceryItem.getQuantity());
    }
    public void UpdateGroceryListQuantity(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List")
                .document(groceryItem.getUpc()).update("quantity", groceryItem.getQuantity());
    }

    public void RemoveAllInventory() {
        firestore.collection("users")
                .document(User.getUserID()).collection("Inventory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                firestore.collection("users").document(User.getUserID())
                                        .collection("Inventory").document(document.getId()).delete();
                            }
                        } else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public void RemoveAllGroceryList() {
        firestore.collection("users")
                .document(User.getUserID()).collection("Grocery List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                firestore.collection("users").document(User.getUserID())
                                        .collection("Grocery List").document(document.getId()).delete();
                            }
                        } else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
    public void addFriend(String uid) {

        getUserFromUid(uid, new MyCallback() {
            @Override
            public void onCallback(String value1) {
                getEmailFromUid(uid, new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        Friend f = new Friend(uid,value1, value);
                        firestore.collection("users").document(User.getUserID()).collection("Sent Friend Requests").document(uid)
                                .set(f);
                    }
                });

            }
        });

      //  Friend f = new Friend(uid, getUserFromUid(uid, new ), "test");



     //   Log.d(TAG, "PLEASE" + getUserFromUid("wDkK2ZYEM8Ob5iSQlNo27G4JKbt2"));

        Friend ff = new Friend(User.getUserID(), User.getUsername(), User.getEmail());
        firestore.collection("users").document(uid).collection("Pending Friend Requests").document(User.getUserID())
                .set(ff);

    }
    public void deleteFriend(String uid) {
        firestore.collection("users").document(User.getUserID()).collection("Friends")
                .document(uid).delete();

    }
    public List<User> queryUserSearch(List<User> userSearchList, UserAdapter adapter, String searchQuery, MyUserSearchCallback myCallback) {
        Log.d(TAG, "CALLING");
        firestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                User holder = document.toObject(User.class);
                                Log.d(TAG,  holder.getUsername() + "==" + searchQuery);
                                Log.d(TAG, "CLASSES: "+ holder.getUsername().getClass() + "==" + searchQuery.getClass());
                                StringBuilder sb = new StringBuilder();
                                char[] letters = holder.getUsername().toCharArray();

                                for (char ch : letters) {
                                    sb.append((byte) ch);
                                }

                                Log.d(TAG,sb.toString());
                                if(holder.getUsername().toString()==searchQuery){
                                    Log.d(TAG,  holder.getUsername() + "==" + searchQuery);
                                    userSearchList.add(document.toObject(User.class));
                                    myCallback.onCallback(userSearchList);
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Log.d(TAG, "NO MATCH BECAUSE " + holder.getUsername() + " != " + searchQuery);
                                }

                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return userSearchList;
    }
    public List<Friend> queryFriendRequests(List<Friend> allFriendRequests, FriendRequestAdapter adapter) {
        firestore.collection("users")
                .document(User.getUserID()).collection("Pending Friend Requests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                allFriendRequests.add(document.toObject(Friend.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return allFriendRequests;
    }
    public List<Friend> queryFriends(List<Friend> allFriends, FriendListAdapter adapter) {
        firestore.collection("users")
                .document(User.getUserID()).collection("Friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                allFriends.add(document.toObject(Friend.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return allFriends;
    }
    public User getUser(String uid){
        final com.groceryapp.upcdata.DB.User.User[] user = new User[1];
        DocumentReference docRef = firestore.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    user[0] = new User(uid, document.get("username").toString(), document.get("email").toString());
                }
                else
                    Log.d(TAG, "get failed with ", task.getException());
            }
        });
        Log.d(TAG, user[0].getUsername());
        return user[0];
    }
    public String getUserFromUid(String uid, MyCallback myCallback){
        DocumentReference docRef = firestore.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, document.getData().toString());
                        Friend f = document.toObject(Friend.class);
                        returnusername = document.getString("username");
                        myCallback.onCallback(returnusername);
                        //    this.Username = dbHelper.getUser(uid).getUsername();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return returnusername;
    }
    public String getEmailFromUid(String uid, MyCallback myCallback){

        DocumentReference docRef = firestore.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, document.getData().toString());
                        Friend f = document.toObject(Friend.class);

                        returnemail = document.getString("email");
                        myCallback.onCallback(returnemail);
                        Log.d(TAG,"EMAIL IS: " + returnemail);
                        //    this.Username = dbHelper.getUser(uid).getUsername();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return returnemail;
    }
    public void acceptFriend(String uid){
        firestore.collection("users").document(User.getUserID()).collection("Pending Friend Requests").document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        firestore.collection("users").document(uid).collection("Sent Friend Requests").document(User.getUserID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        getUserFromUid(uid, new MyCallback() {
            @Override
            public void onCallback(String value1) {
                getEmailFromUid(uid, new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        Friend f = new Friend(uid, value1, value);
                        firestore.collection("users").document(User.getUserID()).collection("Friends").document(uid)
                                .set(f);
                    }
                });

            }
        });

        Friend ff = new Friend(User.getUserID(), User.getUsername(), User.getEmail());
        firestore.collection("users").document(uid).collection("Friends").document(User.getUserID())
                .set(ff);

    }
    public void declineFriend(String uid){

        firestore.collection("users").document(User.getUserID()).collection("Pending Friend Requests").document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        firestore.collection("users").document(uid).collection("Sent Friend Requests").document(User.getUserID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }
}
