package com.example.safesip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import com.example.safesip.notifications.ReminderScheduler;

public class PersonalInformation extends AppCompatActivity {
    private String PERSONAL_DATA_SET_KEY;
    private String USERNAME_KEY;
    private String AGE_KEY;
    private String SEX_KEY;
    private String WEIGHT_KEY;
    private SharedPreferences sharedPreferences;
    private EditText editUsername;
    private EditText editAge;
    private EditText editWeight;
    private RadioGroup RadioGroupbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String PERSONAL_DATA_FILE = "personal-data";

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PERSONAL_DATA_SET_KEY = "personal-data-set";
        USERNAME_KEY = "username";
        AGE_KEY = "age";
        SEX_KEY = "sex";
        WEIGHT_KEY = "weight";

        sharedPreferences = getSharedPreferences(PERSONAL_DATA_FILE, MODE_PRIVATE);
        editUsername = findViewById(R.id.EditTextUsername);
        editAge = findViewById(R.id.EditTextAge);
        editWeight = findViewById(R.id.EditTextWeight);
        RadioGroupbutton = findViewById(R.id.RadioGroupSex);
        Button btnSave = findViewById(R.id.button);

        loadData();

        btnSave.setOnClickListener(this::saveData);
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

    private void saveData(View view) {
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
        if (age < 16 || age > 99) {
            editAge.setError("Age must be between 16 and 99");
            editAge.requestFocus();
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
            if (weight < 40.0 || weight > 300.0) {
                editWeight.setError("Weight must be between 40kg and 300kg");
                editWeight.requestFocus();
                return;
            }

            int selectedId = RadioGroupbutton.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select your sex", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedRadioButton = findViewById(selectedId);
            String sex = selectedRadioButton.getText().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PERSONAL_DATA_SET_KEY, true);
            editor.putString(USERNAME_KEY, name);
            editor.putInt(AGE_KEY, age);
            editor.putFloat(WEIGHT_KEY, weight);
            editor.putString(SEX_KEY, sex);
            editor.apply();

            Intent newActivity = new Intent(getApplicationContext(), MotivQuoteActivity.class);
            startActivity(newActivity);
        }

        private void loadData () {
            if (sharedPreferences.contains(USERNAME_KEY)) {
                editUsername.setText(sharedPreferences.getString(USERNAME_KEY, ""));
            } else {
                editUsername.setText("");
            }
            if (sharedPreferences.contains(AGE_KEY)) {
                String savedAge = String.valueOf(sharedPreferences.getInt(AGE_KEY, 0));
                editAge.setText(savedAge);
            } else {
                editAge.setText("");
            }
            if (sharedPreferences.contains(WEIGHT_KEY)) {
                String savedWeight = String.valueOf(sharedPreferences.getFloat(WEIGHT_KEY, 0f));
                editWeight.setText(savedWeight);
            } else {
                editWeight.setText("");
            }
            if (sharedPreferences.contains(SEX_KEY)) {
                String savedSex = sharedPreferences.getString(SEX_KEY, "");
                for (int i = 0; i < RadioGroupbutton.getChildCount(); i++) {
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