package com.example.safesip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Graph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);
        String alcoolByDay = dataBase.getString("alcoolByDay", "");
        String[] alcoolByDayArray = alcoolByDay.isEmpty() ? new String[0] : alcoolByDay.split(",");

        LineChart chart = findViewById(R.id.alcoolChart);

        List<Entry> entries = new ArrayList<>();

        int dayIndex = 1;
        for (String s : alcoolByDayArray) {
            if (s.isEmpty()) continue;
            try {
                float value = Float.parseFloat(s);
                entries.add(new Entry(dayIndex, value));
                dayIndex++;
            } catch (NumberFormatException ignored) {}
        }

        LineDataSet dataSet = new LineDataSet(entries, "Alcohol per day (ml)");
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.getDescription().setText("History of daily alcohol (ml)");
        chart.getDescription().setTextSize(12f);

        chart.invalidate();
    }

    public void onClickBack(View view) {
        finish();
    }
}
