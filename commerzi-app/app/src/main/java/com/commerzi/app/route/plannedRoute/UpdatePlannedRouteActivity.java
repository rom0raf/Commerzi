package com.commerzi.app.route.plannedRoute;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.commerzi.app.HomeActivity;
import com.commerzi.app.R;
import androidx.appcompat.app.AppCompatActivity;

import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.customers.Customer;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Activity for updating a planned route.
 */
public class UpdatePlannedRouteActivity extends AppCompatActivity {

    private Set<Customer> selectedItems = new HashSet<>();
    private List<Customer> dropdownItems;
    private EditText etName;
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;
    private CustomAdapter adapter;
    private Button btnSave;
    private PlannedRoute route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_planned_route);

        etName = findViewById(R.id.updateRouteNameField);
        btnSave = findViewById(R.id.btnUpdateRoute);
        chipGroup = findViewById(R.id.chipGroupUpdateRoute);

        route = (PlannedRoute) getIntent().getParcelableExtra("route");

        if (route != null) {
            etName.setText(route.getName());
            selectedItems = new HashSet<>(route.getCustomers());

            updateChipGroup();
        }

        btnSave.setOnClickListener(v -> updateRoute(route));

        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.getCustomers(new CommunicatorCallback<>(
                response -> {
                    createCustomersList(response.customers);
                },
                error -> {
                    Toast.makeText(getApplicationContext(), error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    /**
     * Updates the planned route with the new data.
     *
     * @param route The planned route to update.
     */
    private void updateRoute(PlannedRoute route) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());

        route.setName(etName.getText().toString());
        route.setCustomers(new ArrayList<>(selectedItems));

        communicator.updateRoute(route, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(UpdatePlannedRouteActivity.this, response.message, Toast.LENGTH_SHORT).show();
                    Intent intention = new Intent(UpdatePlannedRouteActivity.this, HomeActivity.class);
                    startActivity(intention);
                },
                error -> {
                    Toast.makeText(UpdatePlannedRouteActivity.this, error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    /**
     * Creates the list of customers for the AutoCompleteTextView.
     *
     * @param customers The list of customers.
     */
    private void createCustomersList(ArrayList<Customer> customers){
        autoCompleteTextView = findViewById(R.id.autoCompleteTextViewUpdateRoute);
        chipGroup = findViewById(R.id.chipGroupUpdateRoute);
        String[] allItems = new String[customers.size()];

        for(int i = 0; i < customers.size(); i++){
            allItems[i] = customers.get(i).getName();
        }

        dropdownItems = customers;

        adapter = new CustomAdapter(dropdownItems);

        // Populate the AutoCompleteTextView with all items
        autoCompleteTextView.setAdapter(adapter);

        // Listen for item selection in the AutoCompleteTextView
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Customer selectedItem = adapter.getItem(position);
            if (selectedItems.contains(selectedItem)) {
                selectedItems.remove(selectedItem);
                removeChip(selectedItem);
                adapter.notifyDataSetChanged();
            } else {
                selectedItems.add(selectedItem);
                addChip(selectedItem);
                autoCompleteTextView.setText("");
                adapter.notifyDataSetChanged();
            }
        });

        // Listen for click on AutoCompleteTextView
        autoCompleteTextView.setOnClickListener(v -> {
            autoCompleteTextView.showDropDown();
        });
    }

    /**
     * Adds a chip to the ChipGroup.
     *
     * @param item The customer to add as a chip.
     */
    private void addChip(Customer item) {
        Chip chip = new Chip(this);
        chip.setText(item.getName());
        chip.setCloseIconVisible(true);

        // Listen for click on the close icon of the chip
        chip.setOnCloseIconClickListener(v -> removeItem(item));

        dropdownItems.remove(item);

        chipGroup.addView(chip); // Add the chip to the ChipGroup
    }

    /**
     * Removes an item from the selected items and updates the UI.
     *
     * @param item The customer to remove.
     */
    private void removeItem(Customer item) {
        selectedItems.remove(item);
        dropdownItems.add(item);
        removeChip(item);
        adapter.notifyDataSetChanged();
    }

    /**
     * Updates the ChipGroup with the selected items.
     */
    private void updateChipGroup() {
        chipGroup.removeAllViews(); // Clear the old chips

        for (Customer customer : selectedItems) {
            Chip chip = new Chip(this);
            chip.setText(customer.getName());
            chip.setCloseIconVisible(true);

            // Listen for click on the close icon of the chip
            chip.setOnCloseIconClickListener(v -> removeItem(customer));

            chipGroup.addView(chip);
        }
    }

    /**
     * Removes a chip from the ChipGroup based on the text.
     *
     * @param item The customer to remove.
     */
    private void removeChip(Customer item) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            View child = chipGroup.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                if (chip.getText().toString().equals(item.getName())) {
                    chipGroup.removeView(chip);
                    break;
                }
            }
        }
    }

    /**
     * Custom adapter for the AutoCompleteTextView.
     */
    private class CustomAdapter extends BaseAdapter implements Filterable {
        private List<Customer> data; // List to store the filtered data displayed in the AutoCompleteTextView dropdown
        private List<Customer> originalData; // List to store the original data before filtering
        private ItemFilter itemFilter = new ItemFilter(); // Filter used to perform filtering of data

        CustomAdapter(List<Customer> data) {
            this.data = data;
            this.originalData = new ArrayList<>(data);
        }

        @Override
        public int getCount() {
            return data.size(); // Return the number of items in the filtered data
        }

        @Override
        public Customer getItem(int position) {
            return data.get(position); // Get an item from the filtered data at a given position
        }

        @Override
        public long getItemId(int position) {
            return position; // Get the ID of an item in the filtered data at a given position
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // Inflate the layout for each item in the AutoCompleteTextView dropdown if it's not already created
                convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(getItem(position).getName()); // Set the text of the dropdown item to the current item in the filtered data
            return convertView;
        }

        void remove(String item) {
            data.remove(item); // Remove a specific item from the filtered data
        }

        @Override
        public Filter getFilter() {
            return itemFilter; // Get the custom filter used for filtering data in the AutoCompleteTextView
        }

        /**
         * Custom filter for filtering data in the AutoCompleteTextView.
         */
        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Customer> filteredList = new ArrayList<>();
                if (TextUtils.isEmpty(constraint)) {
                    // If the constraint is empty, show all original data items
                    filteredList.addAll(originalData);
                } else {
                    // Filter the original data based on the constraint (user input)
                    for (Customer item : originalData) {
                        if (!selectedItems.contains(item) && item.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            // Add items to the filtered list that match the constraint and are not selected yet
                            filteredList.add(item);
                        }
                    }
                }
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Update the data with the filtered list and notify the adapter about the changes
                data.clear();
                data.addAll((List<Customer>) results.values);
                notifyDataSetChanged();
            }
        }
    }
}