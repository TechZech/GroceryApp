package com.groceryapp.upcdata.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;
import com.groceryapp.upcdata.LoginStuff.LoginActivity;
import com.groceryapp.upcdata.R;

public class SettingsFragment extends Fragment {

    Activity context;
    Button btnLogOut;
    ImageView ivProfile;
    TextView tvEmail;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    Uri photoUrl = user.getPhotoUrl();


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
            tvEmail = view.findViewById(R.id.tvEmail);

            tvEmail.setText(email);

            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogOut();
                }
            });

            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.download)
                    .transform(new FitCenter(), new RoundedCorners(25))
                    .into(ivProfile);

        }

    public void LogOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(context.getApplicationContext(), LoginActivity.class));
        context.finish();
    }
}
