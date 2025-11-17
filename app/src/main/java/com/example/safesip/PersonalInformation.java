package com.example.safesip;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


public class PersonalInformation extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private EditText editUsername;
    private EditText editAge;
    private EditText editHeight;
    private EditText editWeight;
    private Button btnSave;
    private RadioGroup RadioGroupbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("safe sip", MODE_PRIVATE);
        editUsername = findViewById(R.id.EditTextUsername);
        editAge = findViewById(R.id.EditTextAge);
        editHeight = findViewById(R.id.EditTextHeight);
        editWeight = findViewById(R.id.EditTextWeight);
        RadioGroupbutton = findViewById(R.id.RadioGroupSex);
        btnSave = findViewById(R.id.button);

        loadData();

        btnSave.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        String name = editUsername.getText().toString();
        if (name.isEmpty()) {
            editUsername.setError("Please enter username");
            editUsername.requestFocus();
            return;
        }

        String ageText = editAge.getText().toString();
        if (ageText.isEmpty()) {
            editAge.setError("Please enter age");
            editAge.requestFocus();
            return;
        }
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            editAge.setError("Invalid number");
            editAge.requestFocus();
            return;
        }

        String heightText = editHeight.getText().toString();
        if (heightText.isEmpty()) {
            editHeight.setError("Please enter height");
            editHeight.requestFocus();
            return;
        }
        float height;
        try {
            height = Float.parseFloat(heightText);
        } catch (NumberFormatException e) {
            editHeight.setError("Invalid number");
            editHeight.requestFocus();
            return;
        }

        String weightText = editWeight.getText().toString();
        if (weightText.isEmpty()) {
            editWeight.setError("Please enter weight");
            editWeight.requestFocus();
            return;
        }
        float weight;
        try {
            weight = Float.parseFloat(weightText);
        } catch (NumberFormatException e) {
            editWeight.setError("Invalid weight");
            editWeight.requestFocus();
            return;
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", name);
        editor.putInt("age", age);
        editor.putFloat("height", height);
        editor.putFloat("weight", weight);
        editor.apply();
    }
    private void loadData() {
        if (sharedPreferences.contains("username")) {
            editUsername.setText(sharedPreferences.getString("username", ""));
        } else {
            editUsername.setText("");
        }
        if (sharedPreferences.contains("age")) {
            int savedAge = sharedPreferences.getInt("age", 0);
            editAge.setText(String.valueOf(savedAge));
        } else {
            editAge.setText("");
        }
        if(sharedPreferences.contains("height")) {
            float savedHeight = sharedPreferences.getFloat("height", 0f);
            editHeight.setText(String.valueOf(savedHeight));
        } else {
            editHeight.setText("");
        }
        if (sharedPreferences.contains("weight")) {
            float savedWeight = sharedPreferences.getFloat("weight", 0f);
            editWeight.setText(String.valueOf(savedWeight));
        } else {
            editWeight.setText("");
        }

    }


}