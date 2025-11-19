package com.example.safesip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
        LineDataSet dataSet = new LineDataSet(entries, "Alcohol by day (mL)");
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleRadius(4f);
        dataSet.setCircleHoleRadius(2f);
        dataSet.setColor(Color.parseColor("#3F51B5"));
        dataSet.setCircleColor(Color.parseColor("#3F51B5"));
        dataSet.setHighLightColor(Color.parseColor("#303F9F"));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#3F51B5"));
        dataSet.setFillAlpha(60);
        dataSet.setDrawValues(false);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setNoDataText("I still don't have any history");
        chart.getDescription().setEnabled(false);
        chart.animateX(800);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setAxisLineColor(Color.LTGRAY);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#DDDDDD"));
        leftAxis.setTextColor(Color.DKGRAY);
        leftAxis.setAxisLineColor(Color.TRANSPARENT);
        chart.getAxisRight().setEnabled(false);
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.DKGRAY);
        chart.invalidate();
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(getApplicationContext(), ActionActivity.class);
        startActivity(intent);
    }
}
