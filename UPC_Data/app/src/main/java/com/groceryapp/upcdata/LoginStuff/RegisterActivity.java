package com.groceryapp.upcdata.LoginStuff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.User.User;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.MainActivity;
import com.groceryapp.upcdata.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";

    protected EditText etEmail;
    private EditText etPassword;
    private EditText etUsername;
    private Button btnSignUp;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        getSupportActionBar().hide();

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String username = etUsername.getText().toString();

                if (email.isEmpty()){
                    etEmail.setError("Email is Required");
                    return;
                }
                if (password.isEmpty()){
                    etPassword.setError("Password is Required");
                    return;
                }
                if (username.isEmpty()){
                    etUsername.setError("Username is Required");
                }


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //DB = new DBHelper(RegisterActivity.this);
                            FirebaseUser FirebaseUser = mAuth.getCurrentUser();
                            updateAuthUserName(username, FirebaseUser);
                            User newUser = new User(FirebaseUser.getUid(), username, FirebaseUser.getEmail());
                            createNewFirebaseUserData(newUser, FirebaseUser);

                            //Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();



                          //  User nUser = new User(user.getUid());
                            //s.createUser(nUser);
                            goMainActivity();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Login Text");
                goLoginActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
    private void goLoginActivity() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void createNewFirebaseUserData(User user, FirebaseUser FirebaseUser){
        DocumentReference documentReference = firestore.collection("users").document(FirebaseUser.getUid());
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: user data created for " + FirebaseUser.getUid());
            }
        });
    }

    private void updateAuthUserName(String username, FirebaseUser firebaseUser){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "User Profile Updated" + "username is now " + firebaseUser.getDisplayName());
                        }
                    }
                });
        firebaseUser.reload();
    }
}