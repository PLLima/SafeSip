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

import com.example.safesip.notifications.ReminderScheduler;

public class ActionActivity extends AppCompatActivity {

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
        TextView streakText = findViewById(R.id.StreakText);
        CharSequence streakInformation = "You are on a " + streak;
        switch (Integer.parseInt(streak)) {
            case 0:
                streakInformation += " days streak…";
                break;
            case 1:
                streakInformation += " day streak" + "🔥!";
                break;
            default:
                streakInformation += " days streak" + "🔥!";
        }
        streakText.setText(streakInformation);
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
        share.setOnClickListener(v -> {
            String message = "I’ve kept my alcohol-free streak for " + streak;
            switch (Integer.parseInt(streak)) {
                case 0:
                    message += " days… I could have done better…";
                    break;
                case 1:
                    message += " day! I'm proud of myself!";
                    break;
                default:
                    message += " days! I'm proud of myself!";
            }
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(shareIntent);
        });
    }

    @Override
    protected void onStop() {
        String SETTINGS_FILE = "settings";
        String SETTINGS_DAILY_REMINDER_SET = "daily-reminder-set";
        String SETTINGS_DAILY_REMINDER_HOUR = "daily-reminder-hour";
        String SETTINGS_DAILY_REMINDER_MINUTE = "daily-reminder-minute";
        String PERSONAL_DATA_FILE = "personal-data";
        String PERSONAL_DATA_SET_KEY = "personal-data-set";

        int scheduledHour;
        int scheduledMinute;

        SharedPreferences prefs = getSharedPreferences(PERSONAL_DATA_FILE, MODE_PRIVATE);
        boolean hasPersonalData = prefs.getBoolean(PERSONAL_DATA_SET_KEY, false);

        // Re-add daily reminder everyday at a set hour if it wasn't set before in the same execution of the app
        if (hasPersonalData) {
            SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
            boolean isDailyReminderSet = settings.getBoolean(SETTINGS_DAILY_REMINDER_SET, false);
            if(!isDailyReminderSet) {
                scheduledHour = settings.getInt(SETTINGS_DAILY_REMINDER_HOUR, 12);
                scheduledMinute = settings.getInt(SETTINGS_DAILY_REMINDER_MINUTE, 0);
                ReminderScheduler.scheduleDailyReminder(this, scheduledHour, scheduledMinute);
            }
        }
        super.onStop();
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