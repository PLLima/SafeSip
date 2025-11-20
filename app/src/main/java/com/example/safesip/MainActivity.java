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

import com.example.safesip.notifications.ReminderScheduler;
import com.example.safesip.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private String PERSONAL_DATA_FILE;
    private String PERSONAL_DATA_SET_KEY;
    private String SETTINGS_FILE;
    private String SETTINGS_DAILY_REMINDER_SET;
    private String SETTINGS_DAILY_REMINDER_HOUR;
    private String SETTINGS_DAILY_REMINDER_MINUTE;

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

        SETTINGS_FILE = "settings";
        SETTINGS_DAILY_REMINDER_SET = "daily-reminder-set";
        SETTINGS_DAILY_REMINDER_HOUR = "daily-reminder-hour";
        SETTINGS_DAILY_REMINDER_MINUTE = "daily-reminder-minute";
        PERSONAL_DATA_FILE = "personal-data";
        PERSONAL_DATA_SET_KEY = "personal-data-set";

        int scheduledHour = Constants.scheduledHour;
        int scheduledMinute = Constants.scheduledMinute;

        Button welcomeButton;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        welcomeButton = findViewById(R.id.WelcomeButton);

        SharedPreferences prefs = getSharedPreferences(PERSONAL_DATA_FILE, MODE_PRIVATE);
        boolean hasPersonalData = prefs.getBoolean(PERSONAL_DATA_SET_KEY, false);
        if (hasPersonalData) {
            welcomeButton.setText(R.string.welcome_screen_start);
            welcomeButton.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), MotivQuoteActivity.class);
                startActivity(intent);
            });

            // Add daily reminder everyday at a set hour
            SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
            if (settings.contains(SETTINGS_DAILY_REMINDER_SET)) {
                scheduledHour = settings.getInt(SETTINGS_DAILY_REMINDER_HOUR, 12);
                scheduledMinute = settings.getInt(SETTINGS_DAILY_REMINDER_MINUTE, 30);
            } else {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(SETTINGS_DAILY_REMINDER_SET, true);
                editor.putInt(SETTINGS_DAILY_REMINDER_HOUR, scheduledHour);
                editor.putInt(SETTINGS_DAILY_REMINDER_MINUTE, scheduledMinute);
                editor.apply();
            }
            ReminderScheduler.scheduleDailyReminder(this, scheduledHour, scheduledMinute);
        } else {
            welcomeButton.setText(R.string.welcome_screen_signup);
            welcomeButton.setOnClickListener(v -> {
                Intent newActivity = new Intent(getApplicationContext(), PersonalInformation.class);
                startActivity(newActivity);
            });
        }
    }

    @Override
    protected void onStop() {
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
}