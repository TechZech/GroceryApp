package com.groceryapp.upcdata.LoginStuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groceryapp.upcdata.MainActivity;
import com.groceryapp.upcdata.R;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    protected EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private ImageView ivLogo;
    private TextView tvTitle;
    private TextView tvRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            goMainActivity();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        ivLogo = findViewById(R.id.ivLogo);
        tvTitle = findViewById(R.id.tvTitle);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.isEmpty()){
                    etEmail.setError("Email is Required.");
                    return;
                }
                if (password.isEmpty()){
                    etPassword.setError("Password is required");
                    return;
                }

                loginUser(email, password);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Register Text");
                goRegisterActivity();
            }
        });

    }

    private void loginUser(String email, String password) {
        Log.i(TAG, "Attempting to login user " + email);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "User Logged In Successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    goMainActivity();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goRegisterActivity(){
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
        finish();
    }
}
