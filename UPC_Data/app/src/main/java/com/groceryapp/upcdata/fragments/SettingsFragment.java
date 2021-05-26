package com.groceryapp.upcdata.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.LoginStuff.LoginActivity;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.EditProfileFragment;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.ManageListsFragment;


public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    Activity context = getActivity();
    Button btnLogOut;
    TextView tvEmail;
    TextView tvEmail2;
    TextView tvDisplayName;
    Button btnSettings1;
    Button btnSettings2;
    Button btnSettings3;
    Button friendsListButton;
    Button privacyButton;
    private ImageView ivProfile;
    private ImageView ivEditProfile;
    Button searchRequestButton;

    FirebaseUser FirebaseUser;
    String email;
    Uri userphotoUrl;
    String Username;
    DBHelper DBhelper;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    com.groceryapp.upcdata.DB.User.User User = new User(mAuth);
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_settings, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            btnLogOut = view.findViewById(R.id.btnLogOut);
            ivProfile = view.findViewById(R.id.ivProfile);
            ivEditProfile = view.findViewById(R.id.ivEditProfile);
            tvEmail = view.findViewById(R.id.ownertv);
            tvEmail2 = view.findViewById(R.id.ownertv2);
            tvDisplayName = view.findViewById(R.id.tvGroupName);
            btnSettings1 = view.findViewById(R.id.kickButton);
            privacyButton = view.findViewById(R.id.privacyButton);
            btnSettings2 = view.findViewById(R.id.btnSettings2);
            btnSettings3 = view.findViewById(R.id.btnSettings3);

       //     searchRequestButton = view.findViewById(R.id.searchRequestButton); moved search to friends fragment

            FirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            email = FirebaseUser.getEmail();
            userphotoUrl = FirebaseUser.getPhotoUrl();
            Username = FirebaseUser.getDisplayName();
            DBhelper = new DBHelper();

            tvDisplayName.setText(Username);

            privacyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(privacyButton.getText().toString().equals("PUBLIC")){
                        DBhelper.setUserSetting("Private", User);
                    }
                    else if(privacyButton.getText().toString().equals("PRIVATE")){
                        DBhelper.setUserSetting("Public", User);
                    }
                }
            });
            ivEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(openGalleryIntent, 1000);
                }
            });

            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogOut();
                }
            });

            btnSettings1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new EditProfileFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.slide_out
                            )
                            .replace(R.id.flContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            /*
            searchRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new SearchFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.slide_out
                            )
                            .replace(R.id.flContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });*/
            btnSettings2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new ShoppingHistoryFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.slide_out
                            )
                            .replace(R.id.flContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            DBhelper.fCountFunc(new DBHelper.FriendCountCallback() {
                @Override
                public void OnCallback(int frs) {
                    if(frs==1){
                        tvEmail2.setText(String.valueOf(frs) + " Friend");
                    }
                    else{
                        tvEmail2.setText(String.valueOf(frs) + " Friends");
                    }
                }
            });
            DBhelper.gCountFunc(new DBHelper.GroupCountCallback() {
                @Override
                public void OnCallback(int frs) {
                    if(frs==1){
                        tvEmail.setText(String.valueOf(frs) + " Group");
                    }
                    else{
                        tvEmail.setText(String.valueOf(frs) + " Groups");
                    }
                }
            });
            btnSettings3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new ManageListsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.slide_out
                            )
                            .replace(R.id.flContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            tvEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new FriendListFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.slide_out
                            )
                            .replace(R.id.flContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            tvEmail2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new FriendListFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.slide_out
                            )
                            .replace(R.id.flContainer, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            Glide.with(this)
                    .load(userphotoUrl)
                    .placeholder(R.drawable.download)
                    .into(ivProfile);
        }

    public void LogOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK) {
                    Uri LocalimageUri = data.getData();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference imageRef = storageRef.child(FirebaseUser.getUid());
                    UploadTask uploadTask = imageRef.putFile(LocalimageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.i(TAG, "Upload Success");
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(uri)
                                            .build();

                                    FirebaseUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User Profile Updated");
                                                    } else {
                                                        Log.d(TAG, "Error Updating Profile");
                                                    }
                                                }
                                            });
                                    if(uri != null)
                                        DBhelper.setUserPhotoUrl(uri.toString());
                                }
                            });
                        }
                    });
                }
        }
        Glide.with(this)
                .load(data.getData())
                .into(ivProfile);
    }
}
