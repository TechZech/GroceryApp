package com.groceryapp.upcdata;

import android.text.Editable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryPost;
import com.groceryapp.upcdata.DB.Friend.Friend;
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DB.ShoppingTrip.ShoppingTrip;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.adapters.FriendItemAdapter;
import com.groceryapp.upcdata.adapters.FriendListAdapter;
import com.groceryapp.upcdata.adapters.FriendRequestAdapter;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;
import com.groceryapp.upcdata.adapters.GroceryPostAdapter;
import com.groceryapp.upcdata.adapters.GroupAdapter;
import com.groceryapp.upcdata.adapters.ShoppingTripAdapter;
import com.groceryapp.upcdata.adapters.UserAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {
    public final String TAG = "DBHelper";
    private String returnusername;
    private String returnemail;
    private String returnphotoUrl;
    private Group retGroup = new Group();
    Boolean ret = Boolean.FALSE;
    int frccCount = 0;
    int count = 0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private List<User> retttUser;

    public DBHelper(){
    }
    public interface MyCallback {
        void onCallback(String value);
    }

    public interface twoValueCallback{
        void onCallback(String value, String photoUrl);
    }

    public interface MyUserSearchCallback {
        void onCallback(List<com.groceryapp.upcdata.DB.User.User> value);
    }
    public interface GroceryItemQueryCallback{
        void OnCallback(List<GroceryItem> list, String price);

    }
    public interface AreFriendsCallback{
        void OnCallBack(Boolean value);
    }
    public interface GroupCallback{
        void OnCallback(Group g);
    //    void OnCallback(List<Group> value); //for search

    }
    public interface GroupSearchCallback{
            void OnCallback(List<Group> value); //for search
    }
    public interface SettingCallback{
        void OnCallback(Boolean value);
    }
    public interface isMemberCallback{
        void OnCallback(Boolean b);
    }
    public interface MemberListCallback{
        void OnCallback(List<User> b);
    }
    public interface FriendRequestCountCallback{
        void OnCallback(int frs);
    }

    public interface ShoppingTripCallback{
        void OnCallback(List<ShoppingTrip> trips);
    }
    public void queryGroceryItems(List<GroceryItem> allGroceryItems, GroceryItemAdapter adapter, GroceryItemQueryCallback callback){
        firestore.collection("users")
                .document(User.getUserID()).collection("Grocery List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            double totalPrice = 0.00;
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                GroceryItem groceryItem = document.toObject(GroceryItem.class);
                                allGroceryItems.add(groceryItem);
                                if (!groceryItem.getPrice().equals("N/A")) //Keep this
                                    totalPrice+=(groceryItem.returnPriceAsFloat()*groceryItem.getQuantity());
                            }
                            DecimalFormat precision;
                            if (totalPrice > 100){
                                precision = new DecimalFormat("000.00");
                            }
                            else {
                                precision = new DecimalFormat("0.00");
                            }
                            callback.OnCallback(allGroceryItems, precision.format(totalPrice));
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
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

    public List<GroceryItem> queryFriendInventoryItems(String uid, List<GroceryItem> allInventoryItems, FriendItemAdapter adapter){
        firestore.collection("users")
                .document(uid).collection("Inventory")
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

    public void moveGrocerytoInventory(GroceryItemAdapter adapter){
        firestore.collection("users")
                .document(User.getUserID()).collection("Grocery List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                GroceryItem groceryItem = document.toObject(GroceryItem.class);
                                removeGroceryItem(groceryItem);
                                addInventoryItem(groceryItem);
                            }
                            adapter.clear();
                        }
                    }
                });
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
    public void createNewGroup(Group g){
        DocumentReference groupToGroup = firestore.collection("Groups").document();
        g.setGid(groupToGroup.getId());
        groupToGroup.set(g);

        DocumentReference userToGroup = firestore.collection("users").document(g.getOwner().getUserID()).collection("Groups").document();
        userToGroup.set(g);

    }
    public Group getGroupById(String gid, GroupCallback groupCallback){
        Log.d(TAG,"SHOULD BE FIRST");
/*
        DocumentReference docRef =  firestore.collection("Groups").document(gid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                retGroup = document.toObject(Group.class);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
*/
        DocumentReference docRef = firestore.collection("Groups").document(gid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    retGroup = document.toObject(Group.class);
                    groupCallback.OnCallback(document.toObject(Group.class));
                }
                else
                    Log.d(TAG, "get failed with ", task.getException());
            }
        });

        return retGroup;
    }
    public List<Group> getUserGroups(List<Group> allUserGroups, GroupAdapter adapter) {
        firestore.collection("users")
                .document(User.getUserID()).collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                allUserGroups.add(document.toObject(Group.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return allUserGroups;
    }
    public List<User> queryGroupMembers(List<User> allFriends, Group g, UserAdapter adapter, MemberListCallback callback) {

        DocumentReference docRef = firestore.collection("Groups").document(g.getGid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    retGroup = document.toObject(Group.class);
                    Log.d(TAG, "THE GROUP IS " + g.getMembers().get(0).getUsername());
                    allFriends.add(g.getMembers().get(count));


                    adapter.notifyDataSetChanged();
                }
                else
                    Log.d(TAG, "get failed with ", task.getException());
            }


        });

        callback.OnCallback(allFriends);
        return allFriends;
    }
    public List<GroceryPost> getGroupPosts(Group grroup, List<GroceryPost> allGroupGroceryPosts, GroceryPostAdapter adapter) {
        try {
            firestore.collection("Groups")
                    .document(grroup.getGid()).collection("Posts")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot document : task.getResult()){
                                    Log.d(TAG, document.getId() + "=> " + document.getData());
                                    allGroupGroceryPosts.add(document.toObject(GroceryPost.class));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            else
                                Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return  allGroupGroceryPosts;
    }
    public void inviteToGroup(Group g, User u){
        Log.d(TAG, "INVITE TO GROUP BEING CALLED");
        firestore.collection("users").document(u.getUserID()).collection("Pending Group Invites").document()
                .set(g);
    }
    public void addGroupMember(Group g, User u){

    }

    public boolean areFriends(String FriendAUid, String FriendBUid, AreFriendsCallback Callback){

     //   Log.d(TAG, "AREFRIENDS");
        firestore.collection("users")
                .document(FriendAUid).collection("Friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                   Log.d(TAG, document.getId() + "=> " + document.getData());

                                if(FriendBUid.equals(document.get("userID"))){
                                    firestore.collection("users")
                                            .document(FriendBUid).collection("Friends")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        for (DocumentSnapshot document : task.getResult()){
                                                            Log.d(TAG, document.getId() + "=> " + document.getData());

                                                            if(FriendAUid.equals(document.get("userID"))){
                                                                ret = Boolean.TRUE;
                                                                Callback.OnCallBack(ret);
                                                                Log.d(TAG, "ARE FRIENDS");

                                                            }
                                                            else{
                                                                Log.d(TAG, FriendBUid + "!=" + FriendAUid + "!=" +document.get("userID"));
                                                            }

                                                        }

                                                    }
                                                    else
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            });
                                    Log.d(TAG, "ARE FRIENDS");

                                }
                                else{
                                    Log.d(TAG, FriendBUid + "!=" + "!=" +document.get("userID"));
                                }

                            }

                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });



        return ret;
    }

    public List<GroceryPost> queryFriendFeedItems(List<GroceryPost> FeedItems, GroceryPostAdapter adapter){
        firestore.collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()){

                        Log.d(TAG, document.getId() + "=> " + document.getData());
                        GroceryPost gp = document.toObject(GroceryPost.class);
                        Log.d(TAG, "CURRENT USER: " + User.getUserID() + " USER B: " + gp.user.getUserID());
                      ///  if(User.getUserID().equals(gp.user.getUserID())){
                     ///       FeedItems.add(document.toObject(GroceryPost.class));
                     ////   }


                        areFriends(User.getUserID(), gp.user.getUserID(), new AreFriendsCallback() {
                            @Override
                            public void OnCallBack(Boolean value) {
                                Log.d(TAG,"CALLBACK VALUE IS: " + value);
                                if(value){
                                    FeedItems.add(document.toObject(GroceryPost.class));
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        });


                    }

                }
                else
                    Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        return FeedItems;
    }
    public void addPostItem(GroceryPost groceryPost){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();


        //--------------------------------      Retreive like this ----------------------------------------------------------
     //   Calendar cal = Calendar.getInstance();
      //  cal.setTime(date);
      //  Integer date = cal.get(Calendar.DATE);


        groceryPost.setDateTime(date);
        firestore.collection("Posts").document().set(groceryPost);
    }

    public void addGroceryItem(String itemName, String UPC, String url, int quantity, String price, boolean isInventory){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List").document(UPC)
                .set(new GroceryItem(itemName, UPC, url, quantity, price, isInventory));
    }

    public void addGroceryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Grocery List").document(groceryItem.getUpc())
                .set(groceryItem);
        Log.d(TAG, "GroceryItem Added");
    }

    public void addInventoryItem(String itemName, String UPC, String url, int quantity, String price, boolean isInventory){
        firestore.collection("users").document(User.getUserID()).collection("Inventory").document(UPC)
                .set(new GroceryItem(itemName, UPC, url, quantity, price, isInventory));

        firestore.collection("Posts").document().set(new GroceryPost(User, new GroceryItem(itemName, UPC, url, quantity, price, isInventory), true ));
    }

    public void addInventoryItem(GroceryItem groceryItem){
        firestore.collection("users").document(User.getUserID()).collection("Inventory").document(groceryItem.getUpc())
                .set(groceryItem);
        GroceryPost myGP = new GroceryPost();
        myGP.setGroceryItem(groceryItem);
        myGP.setUser(User);
        addPostItem(myGP); //if user setting for adding to feed automatically is off then you don't want to do this..

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

    public void setGroupSetting(String query, Group g){
        if(query.equals("Public")) {
            DocumentReference docRef = firestore.collection("Groups").document(g.getGid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, document.getData().toString());
                            Group g = document.toObject(Group.class);
                            g.getGroupSettings().setVisibility("Private");
                            docRef.set(g);
                        }
                    }
                }
            });
        }
        else if(query.equals("Private")){
            DocumentReference docRef = firestore.collection("Groups").document(g.getGid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, document.getData().toString());
                            Group g = document.toObject(Group.class);
                            g.getGroupSettings().setVisibility("Public");
                            docRef.set(g);
                        }
                    }
                }
            });
        }
    }
    public void setUserSetting(String query, User ust){
        if(query.equals("Public")) {
            DocumentReference docRef = firestore.collection("users").document(ust.getUserID());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, document.getData().toString());
                            User wtg = document.toObject(User.class);
                            wtg.getUserSettings().setVisibility("Private");
                            docRef.set(wtg);
                        }
                    }
                }
            });
        }
        else if(query.equals("Private")){
            DocumentReference docRef = firestore.collection("users").document(ust.getUserID());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, document.getData().toString());
                            User wtg = document.toObject(User.class);
                            wtg.getUserSettings().setVisibility("Public");
                            docRef.set(wtg);
                        }
                    }
                }
            });
        }
    }
    public boolean querySetting(String query, Group g, SettingCallback settingCallback){
        if(query.equals("visibility") || query.equals("Visible") || query.equals("Visibility") || query.equals("visible")) {
            DocumentReference docRef = firestore.collection("Groups").document(g.getGid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, document.getData().toString());
                            Group g = document.toObject(Group.class);
                            if(g.getGroupSettings().getVisibility().equals("Public")){
                                settingCallback.OnCallback(true);
                            }
                            else{
                                settingCallback.OnCallback(false);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            settingCallback.OnCallback(false);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        settingCallback.OnCallback(false);
                    }
                }
            });
        }
        else if(query.equals("WhoCanPost") || query.equals("whocanpost")){
            DocumentReference docRef = firestore.collection("Groups").document(g.getGid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, document.getData().toString());
                            Group g = document.toObject(Group.class);
                            if(g.getGroupSettings().getWhoCanPost().equals("Owner")){
                                if(User.getUserID().equals(g.getOwner().getUserID())){
                                    settingCallback.OnCallback(true);
                                }
                                else{
                                    settingCallback.OnCallback(false);
                                }

                            }
                            else if(g.getGroupSettings().getWhoCanPost().equals("Everyone")){
                                settingCallback.OnCallback(true);
                            }
                            else if(g.getGroupSettings().getWhoCanPost().equals("Members Only")){
                                for(int jj = 0; jj<g.getMembers().size(); jj++){
                                    if(User.getUserID().equals(g.getMembers().get(jj).getUserID())){
                                        settingCallback.OnCallback(true); //ismember function
                                        break;
                                    }
                                }

                            }
                            else{
                                settingCallback.OnCallback(false);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            settingCallback.OnCallback(false);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        settingCallback.OnCallback(false);
                    }
                }
            });
        }
        return true;

    }

    public boolean queryUserSetting(String query, User usy, SettingCallback settingCallback){
        if(query.equals("visibility") || query.equals("Visible") || query.equals("Visibility") || query.equals("visible")) {
            DocumentReference docRef = firestore.collection("users").document(usy.getUserID());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, document.getData().toString());
                            User ut = document.toObject(User.class);
                            if(ut.getUserSettings().getVisibility().equals("Public")){
                                settingCallback.OnCallback(true);
                            }
                            else{
                                settingCallback.OnCallback(false);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            settingCallback.OnCallback(false);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        settingCallback.OnCallback(false);
                    }
                }
            });
        }

        return true;

    }



    public boolean isMember(User u, Group g, isMemberCallback settingCallback){
            DocumentReference docRef = firestore.collection("Groups").document(g.getGid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, document.getData().toString());
                            Group g = document.toObject(Group.class);
                            for (int jj = 0; jj < g.getMembers().size(); jj++) {
                                if (User.getUserID().equals(g.getMembers().get(jj).getUserID())) {
                                    settingCallback.OnCallback(true); //ismember function
                                    break;
                                }
                            }
                        }

                            else{
                                settingCallback.OnCallback(false);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            settingCallback.OnCallback(false);
                        }
                }
            });
        return true;

    }
    public void addGroupPost(Group g, GroceryPost groceryPost){
        Date date = new Date();
        groceryPost.setDateTime(date);
        querySetting("WhoCanPost", g, new SettingCallback() {
            @Override
            public void OnCallback(Boolean value) {
                if(value==true){
                        firestore.collection("Groups").document(g.getGid()).collection("Posts").document()
                                .set(groceryPost);
            }
        }
        });
    }
    public void addFriend(String uid) {
    if(User.getUserID().equals(uid)){ //add yourself
        return;
    }
    else {


        getUserFromUid(uid, new twoValueCallback() {
            @Override
            public void onCallback(String value1, String photoUrl) {
                getEmailFromUid(uid, new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        Friend f = new Friend(uid, value1, value, returnphotoUrl);
                        firestore.collection("users").document(User.getUserID()).collection("Sent Friend Requests").document(uid)
                                .set(f);
                    }
                });

            }
        });

        //  Friend f = new Friend(uid, getUserFromUid(uid, new ), "test");


        //   Log.d(TAG, "PLEASE" + getUserFromUid("wDkK2ZYEM8Ob5iSQlNo27G4JKbt2"));

        Friend ff = new Friend(User.getUserID(), User.getUsername(), User.getEmail(), User.getProfilePhotoURL());
        firestore.collection("users").document(uid).collection("Pending Friend Requests").document(User.getUserID())
                .set(ff);
    }
    }
    public void deleteFriend(String uid) {
        Log.d(TAG, "UID IS" + uid);
        firestore.collection("users").document(User.getUserID()).collection("Friends")
                .document(uid).delete();

        firestore.collection("users").document(uid).collection("Friends")
                .document(User.getUserID()).delete();
    }
    public List<Group> queryGroupSearch(List<Group> groupSearchList, GroupAdapter adapter, String searchQuery, GroupSearchCallback myCallback) {
        Log.d(TAG, "CALLING");
        firestore.collection("Groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                Group holder = document.toObject(Group.class);
                                Log.d(TAG,  holder.getGroupname() + "==" + searchQuery);
                                Log.d(TAG, "CLASSES: "+ holder.getGroupname().getClass() + "==" + searchQuery.getClass());

                                if(holder.getGroupname().equals(searchQuery)){
                                    Log.d(TAG,  holder.getGroupname() + "==" + searchQuery);
                                        groupSearchList.add(document.toObject(Group.class));
                                   myCallback.OnCallback(groupSearchList);
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Log.d(TAG, "NO MATCH BECAUSE " + holder.getGroupname() + " != " + searchQuery);
                                }

                            }
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        return groupSearchList;
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
                                if(holder.getUsername().equals(searchQuery)){
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
    public int frCountFunc(User usyy, FriendRequestCountCallback frcc){

        firestore.collection("users")
                .document(User.getUserID()).collection("Pending Friend Requests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                frccCount+=1;

                            }
                            frcc.OnCallback(frccCount);
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
      //  frcc.OnCallback(frccCount);
        return frccCount;
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
    public void setUserPhotoUrl(String url){
        firestore.collection("users").document(User.getUserID()).update("photoUrl", url);
    }

    public String getUserFromUid(String uid, twoValueCallback myCallback){
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
                        returnphotoUrl = document.getString("photoUrl");
                        Log.d(TAG, "IN getUserFromUid, photoUrl = " + returnphotoUrl);
                        myCallback.onCallback(returnusername, returnphotoUrl);
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
        getUserFromUid(uid, new twoValueCallback() {
            @Override
            public void onCallback(String value1, String photoUrl) {
                getEmailFromUid(uid, new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        Friend f = new Friend(uid, value1, value, returnphotoUrl);
                        firestore.collection("users").document(User.getUserID()).collection("Friends").document(uid)
                                .set(f);
                        Log.d(TAG, "IN ACCEPT FRIEND, PHOTO URL = " + photoUrl);
                    }
                });

            }
        });

        Friend ff = new Friend(User.getUserID(), User.getUsername(), User.getEmail(), User.getProfilePhotoURL());
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

    public void addShoppingTrip(ShoppingTrip shoppingTrip){
        firestore.collection("users").document(User.getUserID()).collection("Shopping History").document(shoppingTrip.getDate().toString())
                .set(shoppingTrip);
        Log.d(TAG, "ShoppingTrip Added");
    }

    public void queryShoppingTrips(List<ShoppingTrip> shoppingTrips, ShoppingTripAdapter adapter, ShoppingTripCallback callback){
        firestore.collection("users")
                .document(User.getUserID()).collection("Shopping History")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + "=> " + document.getData());
                                shoppingTrips.add(document.toObject(ShoppingTrip.class));
                            }
                            adapter.notifyDataSetChanged();
                            callback.OnCallback(shoppingTrips);
                        }
                        else
                            Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

    }
}
