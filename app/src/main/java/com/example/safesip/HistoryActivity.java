package com.example.safesip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        CalendarView calendarView = findViewById(R.id.calendarView);
        TextView textView = findViewById(R.id.AlcoholHistory);
        SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);
        String alcoolByDay = dataBase.getString("alcoolByDay", "0");
        String[] alcoholByDayArray = alcoolByDay.split(",");
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar today = Calendar.getInstance();
                int todayYear = today.get(Calendar.YEAR);
                int toadyMonth = today.get(Calendar.MONTH);
                int todayDay = today.get(Calendar.DAY_OF_MONTH);

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                long diffMillis = today.getTimeInMillis() - selectedDate.getTimeInMillis();
                int diffDays = (int) (diffMillis / (1000*60*60*24));

                int index = alcoholByDayArray.length - 1 - diffDays;

                String drank = "0";
                if (index >= 0 && index < alcoholByDayArray.length) {
                    drank = alcoholByDayArray[index];
                }

                textView.setText("On " + dayOfMonth + "." + (month+1) + "." + year + " you drank " +drank+ " potions of alcohol.");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}