package com.example.safesip;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request permission to send notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }

        Button welcomeButton;
        String PERSONAL_DATA_FILE = "personal-data";
        String PERSONAL_DATA_SET_KEY = "personal-data-set";

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        welcomeButton = findViewById(R.id.WelcomeButton);

        SharedPreferences prefs = getSharedPreferences(PERSONAL_DATA_FILE, MODE_PRIVATE);
        boolean hasPersonalData = prefs.getBoolean(PERSONAL_DATA_SET_KEY, false);
        if (hasPersonalData) {
            welcomeButton.setText("Start");
            welcomeButton.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), MotivQuoteActivity.class);
                startActivity(intent);
            });
        } else {
            welcomeButton.setText("Sign up");
            welcomeButton.setOnClickListener(v -> {
                Intent newActivity = new Intent(getApplicationContext(), PersonalInformation.class);
                startActivity(newActivity);
            });
        }
    }
}