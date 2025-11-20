package com.example.safesip;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safesip.notifications.ReminderScheduler;
import com.example.safesip.utils.Constants;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MainActivity extends AppCompatActivity {

    private String PERSONAL_DATA_FILE;
    private String PERSONAL_DATA_SET_KEY;
    private String SETTINGS_FILE;
    private String SETTINGS_DAILY_REMINDER_SET;
    private String SETTINGS_DAILY_REMINDER_HOUR;
    private String SETTINGS_DAILY_REMINDER_MINUTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        runBackProcess();
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

    protected void onResume(){
        super.onResume();
        runBackProcess();
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

    void runBackProcess(){
        LocalDate today = LocalDate.now();
        SharedPreferences dataBase = getSharedPreferences("history", MODE_PRIVATE);
        if (!dataBase.contains("year") || !dataBase.contains("month") || !dataBase.contains("day")) {
            SharedPreferences.Editor editor = dataBase.edit();
            editor.putString("year", String.valueOf(today.getYear()));
            editor.putString("month", String.valueOf(today.getMonthValue()));
            editor.putString("day", String.valueOf(today.getDayOfMonth()));
            editor.putString("alcoolByDay", "0");
            editor.putString("strike", "0");
            editor.putString("alreadyDrankToday", "0");
            editor.apply();
            return;
        }
        String year = dataBase.getString("year", "1");
        String month = dataBase.getString("month", "1");
        String day = dataBase.getString("day", "1");
        LocalDate lastDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        Log.d("MIDNIGHT_TEST", "Midnight executed!");
        long diferenceDays = ChronoUnit.DAYS.between(lastDate, today);
        if(!today.equals(lastDate)){
            String alcoolByDay = dataBase.getString("alcoolByDay", "0");
            String lastStreak = dataBase.getString("strike", "0");
            Log.d("MIDNIGHT_TEST", "Midnight executed on a new day!");
            String[] alcoolByDayArray = alcoolByDay.split(",");
            int newStreak = 0;
            if(alcoolByDayArray[alcoolByDayArray.length-1].equals("0")) newStreak = Math.toIntExact(Integer.parseInt(lastStreak) + diferenceDays);
            else newStreak = Math.toIntExact(diferenceDays);
            for(int i = 0 ; i < diferenceDays ; i++){
                if(!alcoolByDay.isEmpty()) alcoolByDay += ",";
                alcoolByDay += "0";
            }
            int todayDay = today.getDayOfMonth();
            int todayMonth = today.getMonthValue();
            int todayYear = today.getYear();
            SharedPreferences.Editor editor = dataBase.edit();
            editor.putString("strike", Integer.toString(newStreak));
            editor.putString("day", Integer.toString(todayDay));
            editor.putString("month", Integer.toString(todayMonth));
            editor.putString("year", Integer.toString(todayYear));
            editor.putString("alcoolByDay", alcoolByDay);
            editor.putString("alreadyDrankToday", "0");
            editor.apply();
        }
    }
}