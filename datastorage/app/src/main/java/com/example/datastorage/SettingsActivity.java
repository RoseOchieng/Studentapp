import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private EditText etUsername, etEmail;
    private SharedPreferences prefs;
    private static final String PREF_FILE = "com.example.myapp.PREFERENCE_FILE_KEY";
    private static final String KEY_USER = "username_key", KEY_EMAIL = "email_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        prefs = getSharedPreferences(PREF_FILE, MODE_PRIVATE);

        etUsername.setText(prefs.getString(KEY_USER, ""));
        etEmail.setText(prefs.getString(KEY_EMAIL, ""));

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            prefs.edit()
                    .putString(KEY_USER, etUsername.getText().toString())
                    .putString(KEY_EMAIL, etEmail.getText().toString())
                    .apply();
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        });
    }
}