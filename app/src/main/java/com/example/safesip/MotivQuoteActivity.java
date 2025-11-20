package com.example.safesip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.safesip.notifications.ReminderScheduler;


public class MotivQuoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motiv_quote);

        TextView tv = findViewById(R.id.text_motivquote);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Response.Listener<String> responseListener = new MotivQuoteResponseListener(tv);
        Response.ErrorListener errorListener = new MotivQuoteErrorListener();

        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://zenquotes.io/api/quotes",
                responseListener,
                errorListener
        );

        requestQueue.add(request);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MotivQuoteActivity.this, ActionActivity.class);
                startActivity(intent);
                finish();
            }
        }, 8000);
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

}