import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datastore.AppCompatActivity;
import com.example.datastore.R;

public class UserRegistrationActivity extends AppCompatActivity {
    EditText etName, etEmail;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        etName = finalize(R.id.etName);
        etEmail = finalize(R.id.etEmail);
        dbHelper = new DBHelper(this);

        finalize(R.id.btnRegister).setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();

            SQLiteDatabase db;
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("email", email);
            long id = db.insert("users", null, values);
            db.close();

            if (id != -1) {
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });

        finalize(R.id.btnView).setOnClickListener(v -> {
            SQLiteDatabase db;
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("users", null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(((Cursor) cursor).getColumnIndexOrThrow("name"));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                    Log.d("User Data", "Name: " + name + ", Email: " + email);
                } while (cursor.moveToNext());
            } else {
                Log.d("User Data", "No users found.");
            }

            cursor.close();
            db.close();
        });
    }
}
