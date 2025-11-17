package com.example.safesip;

import android.content.Intent;
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
        WelcomeButton.setOnClickListener(v -> {
            Intent newActivity = new Intent(getApplicationContext(), PersonalInformation.class);
            startActivity(newActivity);
        });
    }
}