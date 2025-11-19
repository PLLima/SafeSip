package com.example.safesip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdviceActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_advice);

        String stamount = getIntent().getStringExtra("p");
        TextView tv = findViewById(R.id.resume);

        tv.setText("The amount of alcohol in your blood is " + stamount + "g/L");

        double amount = getIntent().getDoubleExtra("amount", 0);


        String message = Conseil.getAllAdvices(amount);
        TextView tv1 = findViewById(R.id.advise);

        tv1.setText(message);



        //


    }
}