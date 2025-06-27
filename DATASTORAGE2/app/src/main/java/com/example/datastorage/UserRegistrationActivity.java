package com.example.datastorage; // Replace with your package name

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log; // For Logcat output
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // To display users, or you can use a ListView/RecyclerView
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText userEmailEditText; // Renamed to avoid clash with SettingsActivity's email
    private Button registerUserButton;
    private Button viewUsersButton;
    private TextView usersDisplayTextView; // For displaying users directly

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration); // Assuming you have activity_user_registration.xml

        nameEditText = findViewById(R.id.nameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        registerUserButton = findViewById(R.id.registerUserButton);
        viewUsersButton = findViewById(R.id.viewUsersButton);
        usersDisplayTextView = findViewById(R.id.usersDisplayTextView); // Make sure this ID exists in your XML

        dbHelper = new DBHelper(this);

        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        viewUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUsers();
            }
        });
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = userEmailEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and Email cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);

        long newRowId = db.insert("users", null, values); // "users" is your table name

        if (newRowId != -1) {
            Toast.makeText(this, "User registered successfully! ID: " + newRowId, Toast.LENGTH_SHORT).show();
            nameEditText.setText("");
            userEmailEditText.setText("");
            viewUsers(); // Refresh the list after registration
        } else {
            Toast.makeText(this, "Error registering user.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void viewUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        StringBuilder usersInfo = new StringBuilder();

        try {
            // Perform a query to get all users
            cursor = db.query("users", // Table name
                    null,          // All columns
                    null,          // No WHERE clause
                    null,          // No WHERE arguments
                    null,          // No GROUP BY
                    null,          // No HAVING
                    null);         // No ORDER BY

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Get column indices
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int emailIndex = cursor.getColumnIndex("email");

                    // Check if columns exist (good practice)
                    int id = (idIndex != -1) ? cursor.getInt(idIndex) : -1;
                    String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "N/A";
                    String email = (emailIndex != -1) ? cursor.getString(emailIndex) : "N/A";

                    // Log to Logcat
                    Log.d("User Data", "ID: " + id + ", Name: " + name + ", Email: " + email);
                    usersInfo.append("ID: ").append(id)
                            .append(", Name: ").append(name)
                            .append(", Email: ").append(email).append("\n");

                } while (cursor.moveToNext());
            } else {
                usersInfo.append("No users registered yet.");
                Log.d("User Data", "No users registered yet.");
            }
        } catch (Exception e) {
            Log.e("User Data", "Error viewing users: " + e.getMessage());
            usersInfo.append("Error loading users.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        usersDisplayTextView.setText(usersInfo.toString());
    }
}