package com.example.recyclerviewapp; // Ensure your package name matches your project

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// MyAdapter extends RecyclerView.Adapter and specifies MyAdapter.MyViewHolder as its ViewHolder type.
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] localDataSet; // The dataset that the adapter will display.

    /**
     * Provides a reference to the type of views that you are using (custom ViewHolder).
     * The ViewHolder holds references to the views for each item in the list.
     * This helps in efficiently updating the content of the views without
     * having to repeatedly use findViewById().
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemTextView; // Reference to the TextView in list_item_layout.xml

        public MyViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View.
            // For this example, we just get the reference to the TextView.
            itemTextView = view.findViewById(R.id.itemTextView); // Match the ID from list_item_layout.xml
        }

        public TextView getItemTextView() {
            return itemTextView;
        }
    }

    /**
     * Initializes the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MyAdapter(String[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    // This method is called when the RecyclerView needs a new ViewHolder to represent an item.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item.
        // We inflate the list_item_layout.xml here.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false); // Match the layout file name

        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    // This method is called to bind data to an existing ViewHolder.
    // It updates the contents of the ViewHolder's views with the data at the given position.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element.
        holder.getItemTextView().setText(localDataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    // This method tells the RecyclerView how many items are in your list.
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}