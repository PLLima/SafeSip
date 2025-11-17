package com.example.safesip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActionActivity extends AppCompatActivity {

    private TextView StreakText;
    private Button TrackButton;
    private Button HistoryButton;
    private Button StatisticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_action);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StreakText = findViewById(R.id.StreakText);
        TrackButton = findViewById(R.id.TrackButton);
        HistoryButton = findViewById(R.id.HistoryButton);
        StatisticsButton = findViewById(R.id.StatisticsButton);
    }

    public void handleClick(View view) {
        Intent newActivity;
        if(view.getId() == TrackButton.getId()) {
            newActivity = new Intent(getApplicationContext(), RegisterDrinkActivity.class);
            startActivity(newActivity);
        } else if(view.getId() == HistoryButton.getId()) {
            newActivity = new Intent(getApplicationContext(), ActionActivity.class);
            startActivity(newActivity);
        } else if(view.getId() == StatisticsButton.getId()) {
            newActivity = new Intent(getApplicationContext(), ActionActivity.class);
            startActivity(newActivity);
        }
    }
}