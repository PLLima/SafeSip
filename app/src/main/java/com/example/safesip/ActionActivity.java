package com.example.safesip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);
        String streak = dataBase.getString("strike", "0");
        StreakText = findViewById(R.id.StreakText);
        StreakText.setText("You are on a " + streak + " days streak!");
        TrackButton = findViewById(R.id.TrackButton);
        HistoryButton = findViewById(R.id.HistoryButton);
        StatisticsButton = findViewById(R.id.StatisticsButton);

        Button b4 = findViewById(R.id.buttonHelp);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionActivity.this, MoreInfoActivity.class);
                startActivity(intent);
            }

        });

        Button share = findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "I’ve kept my alcohol-free streak for " + streak + " days! Proud of myself!";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(shareIntent);
            }
        });



    }

    public void handleClick(View view) {
        Intent newActivity;
        if(view.getId() == TrackButton.getId()) {
            newActivity = new Intent(getApplicationContext(), RegisterDrinkActivity.class);
            startActivity(newActivity);
        } else if(view.getId() == HistoryButton.getId()) {
            newActivity = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(newActivity);
        } else if(view.getId() == StatisticsButton.getId()) {
            newActivity = new Intent(getApplicationContext(), Graph.class);
            startActivity(newActivity);
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent (getApplicationContext(), PersonalInformation.class);
        startActivity(intent);
    }
}