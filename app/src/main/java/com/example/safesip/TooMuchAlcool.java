package com.example.safesip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalTime;

public class TooMuchAlcool extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_too_much_alcool);
        TextView tv = findViewById(R.id.AlcoolQuantityTextView);
        SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);
        String alcoolByDay = dataBase.getString("alcoolByDay", "0");
        String[] alcoolByDayArray = alcoolByDay.split(",");
        float alcoolQuantity = (float) (Float.parseFloat(alcoolByDayArray[alcoolByDayArray.length-1]) * 0.789);
        tv.setText("You have drank " + Float.toString(alcoolQuantity) + "g of alcohol today");
        String timeString = dataBase.getString("times", "0");
        String alcoolString = dataBase.getString("alcool", "0");
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
        for(int i = 0 ; i < timeStringArray.length ; i++){
            int timeInMinuts = minuts - Integer.parseInt(timeStringArray[i]);
            percentageOfAlcoolInBlood += ((Double.parseDouble(alcoolStringArray[i])*0.789)/(distributionFactor * mass)) - (((0.015*timeInMinuts))/60);
        }
        String bacFormatted = String.format("%.2f", percentageOfAlcoolInBlood);
        TextView tvPercentage = findViewById(R.id.PercentageTextView);
        tvPercentage.setText("Now, you have " + bacFormatted + " g/L in your blood");

        Button b1 = findViewById(R.id.button2);

        double finalPercentageOfAlcoolInBlood = percentageOfAlcoolInBlood;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TooMuchAlcool.this, AdviceActivity.class);
                intent.putExtra("amount", finalPercentageOfAlcoolInBlood);
                intent.putExtra("p", bacFormatted);
                startActivity(intent);



            }
        });
    }


}