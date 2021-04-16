package com.groceryapp.upcdata.fragments.InnerSettingsFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.groceryapp.upcdata.LoginStuff.LoginActivity;
import com.groceryapp.upcdata.MainActivity;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.fragments.SettingsFragment;

public class EditProfileFragment extends Fragment {

    public final String TAG = "EditProfileFragment";

    Button btnSaveChanges;
    Button btnDeleteAccount;
    Button btnResetPassword;
    EditText etEditUsername;
    EditText etEditEmail;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String email = user.getEmail();
    Uri userphotoUrl = user.getPhotoUrl();
    String Username = user.getDisplayName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);

        etEditUsername = view.findViewById(R.id.etEditUsername);
        etEditEmail = view.findViewById(R.id.etEditEmail);
        etEditUsername.setText(Username);
        etEditEmail.setText(email);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Reset Password Email Sent!!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "You must Reauthenticate in order to Reset Your Password", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }
                });
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "User Successfully Deleted");
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            getActivity().finish();
                        }
                        else
                            {
                            Toast.makeText(getContext(), "You must Reauthenticate in order to Delete your Account", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }
                });
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEditUsername.getText().toString().equals(user.getDisplayName())) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(etEditUsername.getText().toString())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User Profile Updated");
                                    }
                                }
                            });
                }
                if (!etEditEmail.getText().toString().equals(user.getEmail())){
                    user.updateEmail(etEditEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "User email address updated");
                            }
                            else{
                                Toast.makeText(getContext(), "You must Reauthenticate in order to Reset your Email", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }
                        }
                    });
                }
                Fragment fragment = new SettingsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .setCustomAnimations(

                                R.anim.fade_in,
                                R.anim.slide_out,
                                R.anim.slide_in,
                                R.anim.fade_out
                        )
                        .replace(R.id.flContainer, fragment);
                fragmentTransaction.commit();
            }
        });

    }
}

