package com.example.safesip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safesip.notifications.ReminderScheduler;

import java.time.LocalTime;

public class TooMuchAlcohol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_too_much_alcohol);
        updateScreen();
    }
    protected void onResume(){
        super.onResume();
        updateScreen();
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

    public void updateScreen(){
        TextView tv = findViewById(R.id.AlcoolQuantityTextView);
        SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);
        String alcoolByDay = dataBase.getString("alcoolByDay", "0,0");
        String[] alcoolByDayArray;
        if (alcoolByDay.trim().isEmpty()) {
            alcoolByDayArray = new String[]{"0"};
        } else {
            alcoolByDayArray = alcoolByDay.split(",");
        }
        float alcoolQuantity = (float) (Float.parseFloat(alcoolByDayArray[alcoolByDayArray.length-1]) * 0.789);
        tv.setText(String.format("You drank %.2fg of alcohol today", alcoolQuantity));
        // Centrer le texte DANS le TextView
        tv.setGravity(Gravity.CENTER);
        String timeString = dataBase.getString("times", "0,0");
        String alcoolString = dataBase.getString("alcool", "0,0");
        SharedPreferences personalDataBase = getSharedPreferences("personal-data", MODE_PRIVATE);
        String sex = personalDataBase.getString("sex", "Male");
        double distributionFactor = 0;
        if(sex.equals("Male")) distributionFactor = 0.68;
        else distributionFactor = 0.55;
        LocalTime agora = LocalTime.now();
        int minuts = agora.getHour() * 60 + agora.getMinute();
            String[] timeStringArray = timeString.split(",");
        String[] alcoolStringArray = alcoolString.split(",");
        float mass = personalDataBase.getFloat("weight", 0);
        double percentageOfAlcoolInBlood = 0;
        if(timeString.isEmpty()){
            percentageOfAlcoolInBlood = 0;
        }
        else {
            for (int i = 0; i < timeStringArray.length; i++) {
                int timeInMinuts = minuts - Integer.parseInt(timeStringArray[i]);
                percentageOfAlcoolInBlood += ((Double.parseDouble(alcoolStringArray[i]) * 0.789) / (distributionFactor * mass)) - (((0.015 * timeInMinuts)) / 60);
            }
        }
        if(percentageOfAlcoolInBlood < 0) percentageOfAlcoolInBlood = 0;
        System.out.println("recalc: " + percentageOfAlcoolInBlood);
        String bacFormatted = String.format("%.2f", percentageOfAlcoolInBlood);
        TextView tvPercentage = findViewById(R.id.PercentageTextView);
        tvPercentage.setText("Now, you have " + bacFormatted + " g/L in your blood");
        tvPercentage.setGravity(Gravity.CENTER);

        Button b1 = findViewById(R.id.button2);

        double finalPercentageOfAlcoolInBlood = percentageOfAlcoolInBlood;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TooMuchAlcohol.this, AdviceActivity.class);
                intent.putExtra("amount", finalPercentageOfAlcoolInBlood);
                intent.putExtra("p", bacFormatted);
                startActivity(intent);
            }
        });
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterDrinkActivity.class);
        startActivity(intent);
    }
}