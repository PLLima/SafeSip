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
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String PERSONAL_DATA_FILE;
    private String PERSONAL_DATA_SET_KEY;

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

        PERSONAL_DATA_FILE = "personal-data";
        PERSONAL_DATA_SET_KEY = "personal-data-set";

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
                Intent intent = new Intent(getApplicationContext(), PersonalInformation.class);
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
        scheduleDailyReminder(hasPersonalData, 12, 30);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManagerCompat.from(this).cancelAll();
    }

    @Override
    protected void onStop() {
        SharedPreferences prefs = getSharedPreferences(PERSONAL_DATA_FILE, MODE_PRIVATE);
        boolean hasPersonalData = prefs.getBoolean(PERSONAL_DATA_SET_KEY, false);

        // Re-add daily reminder everyday at a set hour
        scheduleDailyReminder(hasPersonalData, 12, 30);
        super.onStop();
    }

    public void scheduleDailyReminder(boolean isUserRegistered, int hour, int minute) {

        if(isUserRegistered) {
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
        } else {
            WorkManager.getInstance(this)
                    .cancelUniqueWork("daily_reminder");
        }
    }
}