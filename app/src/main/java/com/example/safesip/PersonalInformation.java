package com.example.safesip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PersonalInformation extends AppCompatActivity {
    private String PERSONAL_DATA_FILE;
    private String PERSONAL_DATA_SET_KEY;
    private String USERNAME_KEY;
    private String AGE_KEY;
    private String SEX_KEY;
    private String HEIGHT_KEY;
    private String WEIGHT_KEY;
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

        PERSONAL_DATA_FILE = "personal-data";
        PERSONAL_DATA_SET_KEY = "personal-data-set";
        USERNAME_KEY = "username";
        AGE_KEY = "age";
        SEX_KEY = "sex";
        HEIGHT_KEY = "height";
        WEIGHT_KEY = "weight";

        sharedPreferences = getSharedPreferences(PERSONAL_DATA_FILE, MODE_PRIVATE);
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

        int selectedId = RadioGroupbutton.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select your sex", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedRadioButton = findViewById(selectedId);
        String sex = selectedRadioButton.getText().toString();

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
        editor.putBoolean(PERSONAL_DATA_SET_KEY, true);
        editor.putString(USERNAME_KEY, name);
        editor.putInt(AGE_KEY, age);
        editor.putString(SEX_KEY, sex);
        editor.putFloat(HEIGHT_KEY, height);
        editor.putFloat(WEIGHT_KEY, weight);
        editor.apply();

        Intent newActivity = new Intent(getApplicationContext(), ActionActivity.class);
        startActivity(newActivity);
    }

    private void loadData() {
        if (sharedPreferences.contains(USERNAME_KEY)) {
            editUsername.setText(sharedPreferences.getString(USERNAME_KEY, ""));
        } else {
            editUsername.setText("");
        }
        if (sharedPreferences.contains(AGE_KEY)) {
            int savedAge = sharedPreferences.getInt(AGE_KEY, 0);
            editAge.setText(String.valueOf(savedAge));
        } else {
            editAge.setText("");
        }
        if(sharedPreferences.contains(HEIGHT_KEY)) {
            float savedHeight = sharedPreferences.getFloat(HEIGHT_KEY, 0f);
            editHeight.setText(String.valueOf(savedHeight));
        } else {
            editHeight.setText("");
        }
        if (sharedPreferences.contains(WEIGHT_KEY)) {
            float savedWeight = sharedPreferences.getFloat(WEIGHT_KEY, 0f);
            editWeight.setText(String.valueOf(savedWeight));
        } else {
            editWeight.setText("");
        }
        if (sharedPreferences.contains(SEX_KEY)) {
            String savedSex = sharedPreferences.getString(SEX_KEY, "");
            for (int i = 0; i<RadioGroupbutton.getChildCount(); i++){
                RadioButton rb = (RadioButton) RadioGroupbutton.getChildAt(i);
                if (rb.getText().toString().equals(savedSex)) {
                    rb.setChecked(true);
                    break;
                }
            }
        } else {
            RadioGroupbutton.clearCheck();
        }
    }
}