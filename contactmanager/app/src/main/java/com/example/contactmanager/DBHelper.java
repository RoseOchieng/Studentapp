package com.example.contactmanager; // IMPORTANT: Replace with your actual package name

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ContactManagerDB.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_CONTACTS = "contacts";

    // Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    // SQL statement to create the contacts table
    private static final String SQL_CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + TABLE_CONTACTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_PHONE + " TEXT NOT NULL," +
                    COLUMN_EMAIL + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
        Log.d("DBHelper", "Contacts table created: " + TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
        Log.w("DBHelper", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
    }

    // --- CRUD Operations for Contact ---

    // Add a new contact
    public long addContact(com.example.contactmanager.Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_EMAIL, contact.getEmail());

        long id = db.insert(TABLE_CONTACTS, null, values);
        db.close();
        return id; // Returns the row ID of the newly inserted row, or -1 if an error occurred
    }

    // Get a single contact by ID
    public com.example.contactmanager.Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        com.example.contactmanager.Contact contact = null;

        try {
            cursor = db.query(TABLE_CONTACTS,
                    new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PHONE, COLUMN_EMAIL},
                    COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Check if columns exist before accessing
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);

                String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "N/A";
                String phone = (phoneIndex != -1) ? cursor.getString(phoneIndex) : "N/A";
                String email = (emailIndex != -1) ? cursor.getString(emailIndex) : "N/A";

                contact = new com.example.contactmanager.Contact(cursor.getInt(Integer.parseInt(cursor.getColumnName(COLUMN_ID))), name, phone, email);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return contact;
    }

    // Get all contacts
    public List<com.example.contactmanager.Contact> getAllContacts() {
        List<com.example.contactmanager.Contact> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    // Check if columns exist before accessing
                    int idIndex = cursor.getColumnIndex(COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                    int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                    int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);

                    int id = (idIndex != -1) ? cursor.getInt(idIndex) : -1;
                    String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "N/A";
                    String phone = (phoneIndex != -1) ? cursor.getString(phoneIndex) : "N/A";
                    String email = (emailIndex != -1) ? cursor.getString(emailIndex) : "N/A";

                    com.example.contactmanager.Contact contact = new com.example.contactmanager.Contact(id, name, phone, email);
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return contactList;
    }

    // Update a contact
    public int updateContact(com.example.contactmanager.Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_EMAIL, contact.getEmail());

        // updating row
        int rowsAffected = db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete a contact
    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}