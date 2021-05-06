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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.LoginStuff.LoginActivity;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.fragments.InnerSettingsFragments.EditProfileFragment;


public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    Activity context = getActivity();
    Button btnLogOut;
    TextView tvEmail;
    TextView tvDisplayName;
    Button btnSettings1;
    Button btnSettings2;
    Button btnSettings3;
    Button friendsListButton;
    private ImageView ivProfile;
    private ImageView ivEditProfile;
    Button searchRequestButton;

    FirebaseUser user;
    String email;
    Uri userphotoUrl;
    String Username;
    DBHelper DBhelper;

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
            tvEmail = view.findViewById(R.id.tvEmail);
            tvDisplayName = view.findViewById(R.id.tvDisplayName);
            btnSettings1 = view.findViewById(R.id.btnSettings1);
            btnSettings2 = view.findViewById(R.id.btnSettings2);
            btnSettings3 = view.findViewById(R.id.btnSettings3);
            friendsListButton = view.findViewById(R.id.friendsButton);
            searchRequestButton = view.findViewById(R.id.searchRequestButton);

            user = FirebaseAuth.getInstance().getCurrentUser();
            email = user.getEmail();
            userphotoUrl = user.getPhotoUrl();
            Username = user.getDisplayName();
            DBhelper = new DBHelper();

            tvEmail.setText(email);
            tvDisplayName.setText(Username);

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

            friendsListButton.setOnClickListener(new View.OnClickListener() {
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
            });
            btnSettings2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBhelper.RemoveAllInventory();
                }
            });


            btnSettings3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBhelper.RemoveAllGroceryList();
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
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri LocalimageUri = data.getData();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imageRef = storageRef.child(user.getUid());
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

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.d(TAG, "User Profile Updated");
                                                }
                                                else{
                                                    Log.d(TAG, "Error Updating Profile");
                                                }
                                            }
                                        });
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
