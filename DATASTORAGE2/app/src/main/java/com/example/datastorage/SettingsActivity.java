package com.example.datastorage; // Replace with your package name

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private Button saveSettingsButton;
    private SharedPreferences sharedPreferences;

    // Keys for SharedPreferences
    private static final String PREFS_NAME = "UserSettings";
    private static final String USERNAME_KEY = "username_key";
    private static final String EMAIL_KEY = "email_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // Assuming you have activity_settings.xml

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Read and display saved settings
        loadSettings();

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void loadSettings() {
        // Retrieve values from SharedPreferences
        String savedUsername = sharedPreferences.getString(USERNAME_KEY, "");
        String savedEmail = sharedPreferences.getString(EMAIL_KEY, "");

        // Set the EditText fields with the retrieved values
        usernameEditText.setText(savedUsername);
        emailEditText.setText(savedEmail);
    }

    private void saveSettings() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        // Use SharedPreferences.Editor to putString values
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, username);
        editor.putString(EMAIL_KEY, email);
        editor.apply(); // Asynchronously save the changes

        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
    }
}