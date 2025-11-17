package com.example.safesip;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class RegisterDrinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_drink);
        SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);
        String drinksString = dataBase.getString("drinks", "");
        if (!drinksString.isEmpty()) {
            updateScreen(drinksString);
        }
        String strike = dataBase.getString("strike", "0");
        Button b = findViewById(R.id.strikeButton);
        b.setText("Check yours " + strike + " days strike");
        MidnightScheduler.scheduleNextMidnight(this);
    }

    public void onClickRegister(View view) {
        EditText drinkNameEditText = findViewById(R.id.drinkName);
        EditText drinkAmountEditText = findViewById(R.id.drinkAmount);
        EditText drinkPercentageEditText = findViewById(R.id.drinkPercentage);
        String drinkName = String.valueOf(drinkNameEditText.getText());
        String drinkAmount = String.valueOf(drinkAmountEditText.getText());
        String drinkPercentage = String.valueOf(drinkPercentageEditText.getText());
        TextView registeredTextView= findViewById(R.id.drinkRegistered);
        if (drinkName.isEmpty() || drinkAmount.isEmpty() || drinkPercentage.isEmpty()) {
            registeredTextView.setText("Please, fill every field before proceding!");
        }
        else{;
            registeredTextView.setText("Drink Registered!");
        }
        SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);
        Map<String, ?> map = dataBase.getAll();
        String drinksString = (String) map.get("drinks");
        if(drinksString == null) drinksString = "";
        String[] drinksArray = drinksString.split(",");
        if(drinksArray.length > 4){
            registeredTextView.setText("Please remove one drink before adding a new one!");
            registeredTextView.setVisibility(VISIBLE);
            (new Handler()).postDelayed(() -> registeredTextView.setVisibility(INVISIBLE), 3000);
            return;
        }
        for(String s : drinksArray){
            if(s.equals(drinkName) && !drinkName.isEmpty()){
                registeredTextView.setText("Drink already registered!");
                registeredTextView.setVisibility(VISIBLE);
                (new Handler()).postDelayed(() -> registeredTextView.setVisibility(INVISIBLE), 3000);
                return;
            }
        }
        registeredTextView.setVisibility(VISIBLE);
        (new Handler()).postDelayed(() -> registeredTextView.setVisibility(INVISIBLE), 3000);
        String newDrinksString = drinksString + "," + drinkName;
        SharedPreferences.Editor editor = dataBase.edit();
        editor.putString("drinks" , newDrinksString);
        editor.apply();
        SharedPreferences newDrinkDB = getSharedPreferences(drinkName, Context.MODE_PRIVATE);
        editor = newDrinkDB.edit();
        editor.putString("amount", drinkAmount);
        editor.putString("percentage", drinkPercentage);
        editor.apply();
        updateScreen(newDrinksString);
    }

    public void updateScreen(String drinksString){
        LinearLayout layout = findViewById(R.id.registerDrink);
        int countButtons = layout.getChildCount() - 7;
        for (int i = 0; i < countButtons; i++) {
            int lastIndex = layout.getChildCount() - 1;
            layout.removeViewAt(lastIndex);
        }
        if (drinksString == null || drinksString.isEmpty()) return;
        String[] drinksArray = drinksString.split(",");
        for (String drink : drinksArray) {
            if (drink.isEmpty()) continue;
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER);
            Button drinkButton = getButton(drink);
            Button removeDrink = new Button(this);
            removeDrink.setText("-");
            removeDrink.setOnClickListener(v -> {
                SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);
                String current = dataBase.getString("drinks", "");
                StringBuilder sb = new StringBuilder();
                if (!current.isEmpty()) {
                    String[] arr = current.split(",");
                    for (String d : arr) {
                        if (d.isEmpty() || d.equals(drink)) continue;
                        if (sb.length() > 0) sb.append(",");
                        sb.append(d);
                    }
                }
                String newDrinksString = sb.toString();
                SharedPreferences.Editor editor = dataBase.edit();
                editor.putString("drinks", newDrinksString);
                editor.apply();
                deleteSharedPreferences(drink);
                updateScreen(newDrinksString);
            });
            row.addView(drinkButton);
            row.addView(removeDrink);
            layout.addView(row);
        }
    }

    @NonNull
    private Button getButton(String drink) {
        Button drinkButton = new Button(this);
        drinkButton.setText(drink);
        drinkButton.setOnClickListener(v -> {
            SharedPreferences dataBase = getSharedPreferences("history", Context.MODE_PRIVATE);

            String alcoolByDay = dataBase.getString("alcoolByDay", "");
            String alreadyDrinkedToday = dataBase.getString("alreadyDrinkedToday", "0");

            String[] alcoolByDayArray =
                    alcoolByDay.isEmpty() ? new String[0] : alcoolByDay.split(",");

            SharedPreferences drinkDB = getSharedPreferences(drink, Context.MODE_PRIVATE);

            float amount = Float.parseFloat(drinkDB.getString("amount","0"));
            float percentage = Float.parseFloat(drinkDB.getString("percentage","0"));
            float qntAlcool = amount * percentage / 100;

            StringBuilder newAlcoolByDay = new StringBuilder();

            if ("1".equals(alreadyDrinkedToday)) {
                if (alcoolByDayArray.length == 0) {
                    newAlcoolByDay.append(qntAlcool);
                } else {
                    for (int i = 0; i < alcoolByDayArray.length - 1; i++) {
                        if (alcoolByDayArray[i].isEmpty()) continue;
                        if (newAlcoolByDay.length() > 0) newAlcoolByDay.append(",");
                        newAlcoolByDay.append(alcoolByDayArray[i]);
                    }
                    float alcoolToday = 0f;
                    String last = alcoolByDayArray[alcoolByDayArray.length - 1];
                    if (!last.isEmpty()) {
                        alcoolToday = Float.parseFloat(last);
                    }
                    alcoolToday += qntAlcool;
                    if (newAlcoolByDay.length() > 0) newAlcoolByDay.append(",");
                    newAlcoolByDay.append(alcoolToday);
                }
            }
            else {
                for (String s : alcoolByDayArray) {
                    if (s.isEmpty()) continue;
                    if (newAlcoolByDay.length() > 0) newAlcoolByDay.append(",");
                    newAlcoolByDay.append(s);
                }
                if (newAlcoolByDay.length() > 0) newAlcoolByDay.append(",");
                newAlcoolByDay.append(qntAlcool);
                SharedPreferences.Editor flagEditor = dataBase.edit();
                flagEditor.putString("alreadyDrinkedToday", "1");
                flagEditor.apply();
            }
            SharedPreferences.Editor editor = dataBase.edit();
            editor.putString("alcoolByDay", newAlcoolByDay.toString());
            editor.apply();
        });
        return drinkButton;
    }

    public void onClickStrike(View view) {
        //vai pra página do grafico
    }
}