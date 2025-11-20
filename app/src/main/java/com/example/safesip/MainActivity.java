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
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.safesip.notifications.DailyReminderWorker;
import com.example.safesip.utils.Constants;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String PERSONAL_DATA_FILE;
    private String PERSONAL_DATA_SET_KEY;
    private String SETTINGS_FILE;
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

        // Add daily reminder everyday at a set hour
        if(hasPersonalData) {
            SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
            if (settings.contains(SETTINGS_DAILY_REMINDER_HOUR)) {
                scheduledHour = settings.getInt(SETTINGS_DAILY_REMINDER_HOUR, 12);
                scheduledMinute = settings.getInt(SETTINGS_DAILY_REMINDER_MINUTE, 30);
            } else {
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(SETTINGS_DAILY_REMINDER_HOUR, scheduledHour);
                editor.putInt(SETTINGS_DAILY_REMINDER_MINUTE, scheduledMinute);
                editor.apply();
            }
            scheduleDailyReminder(scheduledHour, scheduledMinute);
        }
    }

    @Override
    protected void onStop() {
        int scheduledHour;
        int scheduledMinute;

        SharedPreferences prefs = getSharedPreferences(PERSONAL_DATA_FILE, MODE_PRIVATE);
        boolean hasPersonalData = prefs.getBoolean(PERSONAL_DATA_SET_KEY, false);

        // Re-add daily reminder everyday at a set hour
        if (hasPersonalData) {
            SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
            scheduledHour = settings.getInt(SETTINGS_DAILY_REMINDER_HOUR, 12);
            scheduledMinute = settings.getInt(SETTINGS_DAILY_REMINDER_MINUTE, 0);
            scheduleDailyReminder(scheduledHour, scheduledMinute);
        }
        super.onStop();
    }

    public void scheduleDailyReminder(int hour, int minute) {

        long currentTime = System.currentTimeMillis();

        Calendar now = Calendar.getInstance();
        Calendar next = Calendar.getInstance();

        next.set(Calendar.HOUR_OF_DAY, hour);
        next.set(Calendar.MINUTE, minute);
        next.set(Calendar.SECOND, 0);

        if (next.before(now)) {
            next.add(Calendar.DAY_OF_MONTH, 1);
        }

        long initialDelay = next.getTimeInMillis() - currentTime;

        WorkManager.getInstance(this).cancelUniqueWork("daily_reminder");
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(DailyReminderWorker.class, 24, TimeUnit.HOURS)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "daily_reminder",
                        ExistingPeriodicWorkPolicy.UPDATE,
                        request
                );
    }
}