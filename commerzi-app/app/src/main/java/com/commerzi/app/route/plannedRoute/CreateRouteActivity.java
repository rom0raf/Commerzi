package com.commerzi.app.route.plannedRoute;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;

import com.commerzi.app.HomeActivity;
import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;
import com.commerzi.app.customers.Customer;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateRouteActivity extends AppCompatActivity {
    private Set<Customer> selectedItems = new HashSet<>();
    private List<Customer> dropdownItems;
    private AutoCompleteTextView autoCompleteTextView;
    private ChipGroup chipGroup;
    private CustomAdapter adapter;
    private Button btnValidate;
    private EditText routeNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_route);
        
        routeNameField = findViewById(R.id.routeNameField);

        btnValidate = findViewById(R.id.btnValidateCreateRoute);
        btnValidate.setOnClickListener(this::onValidateButtonClicked);

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

    private void onValidateButtonClicked(View view) {
        Log.d("CreateRouteActivity", "onValidateButtonClicked");
        ArrayList<Customer> customersList = new ArrayList<>(selectedItems);
        String name = routeNameField.getText().toString();
        PlannedRoute route = new PlannedRoute(name, customersList);

        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.error_no_route_name, Toast.LENGTH_SHORT).show();
            return;
        }

        if (customersList.isEmpty() || customersList.size() < 2) {
            Toast.makeText(getApplicationContext(), R.string.error_not_enough_customers, Toast.LENGTH_SHORT).show();
            return;
        }

        if (customersList.size() > 8) {
            Toast.makeText(getApplicationContext(), R.string.error_too_much_customers, Toast.LENGTH_SHORT).show();
            return;
        }

        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.createRoute(name, route, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(getApplicationContext(), response.message, Toast.LENGTH_SHORT).show();
                    Intent intention = new Intent(CreateRouteActivity.this, HomeActivity.class);
                    startActivity(intention);
                },
                error -> {
                    Toast.makeText(getApplicationContext(), error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    private void createCustomersList(ArrayList<Customer> customers){
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        chipGroup = findViewById(R.id.chipGroup);
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

    // Method to add a chip to the ChipGroup
    private void addChip(Customer item) {
        Chip chip = new Chip(this);
        chip.setText(item.getName());
        chip.setCloseIconVisible(true);

        // Listen for click on the close icon of the chip
        chip.setOnCloseIconClickListener(v -> removeItem(item));

        dropdownItems.remove(item);

        chipGroup.addView(chip); // Add the chip to the ChipGroup
    }

    // Method to remove an item from the selected items and update the UI
    private void removeItem(Customer item) {
        selectedItems.remove(item);
        dropdownItems.add(item);
        removeChip(item);
        adapter.notifyDataSetChanged();
    }

    // Method to remove a chip from the ChipGroup based on the text
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
