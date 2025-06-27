package com.example.contactmanager; // IMPORTANT: Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditContactActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail;
    private Button buttonSaveContact;
    private DBHelper dbHelper;
    private int contactId = -1; // -1 indicates adding a new contact, otherwise it's an existing contact's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact); // Links to activity_add_edit_contact.xml

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSaveContact = findViewById(R.id.buttonSaveContact);

        dbHelper = new DBHelper(this);

        // Check if we are editing an existing contact
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("contact_id")) {
            contactId = intent.getIntExtra("contact_id", -1);
            if (contactId != -1) {
                // Load contact data for editing
                com.example.contactmanager.Contact contact = dbHelper.getContact(contactId);
                if (contact != null) {
                    editTextName.setText(contact.getName());
                    editTextPhone.setText(contact.getPhone());
                    editTextEmail.setText(contact.getEmail());
                    buttonSaveContact.setText("Update Contact"); // Change button text for edit mode
                    setTitle("Edit Contact"); // Change activity title
                } else {
                    // Contact not found, revert to add mode
                    contactId = -1;
                    setTitle("Add New Contact");
                }
            }
        } else {
            setTitle("Add New Contact");
        }

        buttonSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });
    }

    private void saveContact() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        com.example.contactmanager.Contact contact = new com.example.contactmanager.Contact(name, phone, email);
        long result;

        if (contactId == -1) {
            // Add new contact
            result = dbHelper.addContact(contact);
            if (result != -1) {
                Toast.makeText(this, "Contact added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add contact.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing contact
            contact.setId(contactId); // Set the ID for the contact to be updated
            int rowsAffected = dbHelper.updateContact(contact);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Contact updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update contact.", Toast.LENGTH_SHORT).show();
            }
        }
        finish(); // Go back to MainActivity after saving/updating
    }
}