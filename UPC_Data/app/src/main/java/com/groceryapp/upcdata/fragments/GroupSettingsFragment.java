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
import com.groceryapp.upcdata.DB.Group.Group;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;

public class GroupSettingsFragment extends Fragment {

    public static final String TAG = "GroupSettingsFragment";

    Activity context = getActivity();
    private ImageView ivProfile;
    FirebaseUser user;
    String email;
String userphotoUrl;
    ImageView ivEditProfile;
    String Username;
    DBHelper Dbhelper;
    Button kickButton;
    TextView tvGroupname;
    Button btnGoBack;
    Button settingsButton;
    Group grr;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        Username = user.getDisplayName();
        Dbhelper = new DBHelper();
        tvGroupname = view.findViewById(R.id.tvGroupName);
        kickButton = view.findViewById(R.id.kickButton);
        settingsButton = view.findViewById(R.id.visibilityButton);
        ivEditProfile = view.findViewById(R.id.ivEditProfile);
        ivProfile = view.findViewById(R.id.ivProfile);
        btnGoBack = view.findViewById(R.id.btnGoBackFromGroupSettings);
        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settingsButton.getText().toString().equals("PUBLIC")){
                    Dbhelper.setGroupSetting("Private", grr);
                }
                else if(settingsButton.getText().toString().equals("PRIVATE")){
                    Dbhelper.setGroupSetting("Public", grr);
                }
            }
        });

        kickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group gg = grr;
                Bundle bundle = new Bundle();
                String gidString = gg.getGid();
                bundle.putString("gid", gidString );
                bundle.putBoolean("fromInventory", true);
                Fragment fragment = new GroupMemberListFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .replace(R.id.flContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
unpackBundle();
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
                if(grr!=null){
                    StorageReference imageRef = storageRef.child(grr.getGid());
                    UploadTask uploadTask = imageRef.putFile(LocalimageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.i(TAG, "Upload Success");
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    if(uri != null)
                                        grr.setPhotoUrl(uri.toString());
                                    //         Dbhelper.setUserPhotoUrl(uri.toString());
                                }
                            });
                        }
                    });
                }


            }
        }
        Glide.with(this)
                .load(data.getData())
                .into(ivProfile);

    }
    private boolean unpackBundle(){
        Bundle Args = getArguments();
        Log.d(TAG, "ARGS STRING IS " + Args.getString("gid"));

        Dbhelper.getGroupById(Args.getString("gid"), new DBHelper.GroupCallback() {
            @Override
            public void OnCallback(Group g) {
                grr = g;
                tvGroupname.setText(g.getGroupname());
                userphotoUrl = grr.getPhotoUrl();
                Glide.with(getContext())
                        .load(userphotoUrl)
                        .placeholder(R.drawable.download)
                        .into(ivProfile);

            }
        });
        {

        }
        //grr = dbHelper.getGroupById(Args.getString("gid"));
        Log.d(TAG, "TESTESTEST");
        return Args.getBoolean("fromSearch");
    }
}
