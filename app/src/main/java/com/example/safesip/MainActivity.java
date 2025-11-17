package com.example.safesip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button WelcomeButton;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        WelcomeButton = findViewById(R.id.WelcomeButton);


        SharedPreferences prefs = getSharedPreferences("safe sip", MODE_PRIVATE);
        boolean hasData = prefs.getBoolean("hasData", false);
        if (hasData) {
            Intent intent = new Intent(getApplicationContext(), ActionActivity.class);
            startActivity(intent);
            finish();
        } else {
            WelcomeButton.setOnClickListener(v -> {
                Intent newActivity = new Intent(getApplicationContext(), PersonalInformation.class);
                startActivity(newActivity);
            });
        }
    }
}