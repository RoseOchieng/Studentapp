package com.example.contactmanager; // IMPORTANT: Replace with your actual package name

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private Context context;
    private DBHelper dbHelper;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.dbHelper = new DBHelper(context); // Initialize DB helper here for delete operations
    }

    // Method to update the contact list and refresh the RecyclerView
    public void updateContacts(List<Contact> newContactList) {
        this.contactList = newContactList;
        notifyDataSetChanged(); // Notifies the adapter that the data set has changed
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single contact item
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        // Get the Contact object at the current position
        Contact contact = contactList.get(position);

        // Bind data to TextViews
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhone());
        holder.emailTextView.setText(contact.getEmail());

        // Set a default "profile photo" (first letter of name)
        if (contact.getName() != null && !contact.getName().isEmpty()) {
            holder.profilePhotoTextView.setText(String.valueOf(contact.getName().charAt(0)).toUpperCase());
        } else {
            holder.profilePhotoTextView.setText("?");
        }

        // --- Implicit Intents for Call/Email ---

        // Phone icon click listener to make a call
        holder.callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to dial the phone number
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact.getPhone()));
                if (callIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(callIntent);
                } else {
                    Toast.makeText(context, "No app to handle calls.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Email icon click listener to send an email
        holder.emailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to send an email
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contact.getEmail()}); // recipient
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding your contact"); // subject
                if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(emailIntent);
                } else {
                    Toast.makeText(context, "No app to handle emails.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Long press to edit/delete contact
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showEditDeleteDialog(contact);
                return true; // Consume the long click event
            }
        });

        // Short click to edit contact
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddEditContactActivity in edit mode
                Intent intent = new Intent(context, AddEditContactActivity.class);
                intent.putExtra("contact_id", contact.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // ViewHolder class to hold references to the views for each item
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView emailTextView;
        TextView profilePhotoTextView; // For the initial/placeholder photo
        ImageView callImageView;
        ImageView emailImageView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewContactName);
            phoneTextView = itemView.findViewById(R.id.textViewContactPhone);
            emailTextView = itemView.findViewById(R.id.textViewContactEmail);
            profilePhotoTextView = itemView.findViewById(R.id.textViewProfilePhoto);
            callImageView = itemView.findViewById(R.id.imageViewCall);
            emailImageView = itemView.findViewById(R.id.imageViewEmail);
        }
    }

    private void showEditDeleteDialog(final Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options");
        builder.setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
            switch (which) {
                case 0: // Edit
                    Intent intent = new Intent(context, AddEditContactActivity.class);
                    intent.putExtra("contact_id", contact.getId());
                    context.startActivity(intent);
                    break;
                case 1: // Delete
                    // Confirm deletion
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Contact")
                            .setMessage("Are you sure you want to delete " + contact.getName() + "?")
                            .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                                dbHelper.deleteContact(contact.getId());
                                // Refresh the list after deletion
                                if (context instanceof MainActivity) {
                                    ((MainActivity) context).onResume(); // A simple way to trigger refresh
                                }
                                Toast.makeText(context, "Contact deleted.", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                    break;
            }
        });
        builder.show();
    }
}