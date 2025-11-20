package com.example.safesip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safesip.notifications.ReminderScheduler;

public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_more_info);


        Button btn = findViewById(R.id.emergency);

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:112"));
            startActivity(intent);
        });

        Button btn2 = findViewById(R.id.open_help);

        btn2.setOnClickListener(v -> {
            String url = "https://www.alcool-info-service.fr/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
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

    public void onClickBack(View view) {
        Intent intent = new Intent(getApplicationContext(), ActionActivity.class);
        startActivity(intent);
    }

}
