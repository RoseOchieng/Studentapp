public class SettingsActivity extends AppCompatActivity {
    EditText etUsername, etEmail;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Load saved values
        etUsername.setText(prefs.getString("username_key", ""));
        etEmail.setText(prefs.getString("email_key", ""));

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username_key", etUsername.getText().toString());
            editor.putString("email_key", etEmail.getText().toString());
            editor.apply();

            Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
        });
    }
}
