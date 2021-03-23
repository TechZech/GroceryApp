package com.groceryapp.upcdata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.groceryapp.upcdata.LoginStuff.LoginActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;

    EditText editTextName;
    Button btnClickHere;
    TextView textName;
    Scraper myScrap = new Scraper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView wb = (WebView) findViewById(R.id.webview);
        wb.loadUrl("file:///android_asset/index.html");
        editTextName = (EditText) findViewById(R.id.upcData);
        btnClickHere = (Button) findViewById(R.id.sub);
        btnLogOut = findViewById(R.id.btnLogOut);
        textName = (TextView) findViewById(R.id.retData);
        btnClickHere.setVisibility(View.INVISIBLE);
        btnClickHere.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String upccode = editTextName.getText().toString();
            //    textName.setText(name);
                try {
                    textName.setText(myScrap.getUPCData(upccode));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });
    }

    public void LogOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}