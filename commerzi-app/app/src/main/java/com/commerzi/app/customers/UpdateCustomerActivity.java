package com.commerzi.app.customers;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

public class UpdateCustomerActivity extends AppCompatActivity {

    private EditText etName, etAddress, etCity, etDescription, etFirstName, etLastName, etPhoneNumber;
    private Button btnSave;
    private Customer customer;
    private RadioButton radioCustomer, radioProspect;
    private RadioGroup rgCustomerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_customer);

        etName = findViewById(R.id.nameEditField);
        etAddress = findViewById(R.id.addressEditField);
        etCity = findViewById(R.id.cityEditField);
        etDescription = findViewById(R.id.descriptionEditField);
        etFirstName = findViewById(R.id.firstnameEditField);
        etLastName = findViewById(R.id.lastnameEditField);
        etPhoneNumber = findViewById(R.id.phoneNumberEditField);
        rgCustomerType = findViewById(R.id.radioGroupEditCustomerType);
        radioCustomer = findViewById(R.id.radioEditCustomer);
        radioProspect = findViewById(R.id.radioEditProspect);
        btnSave = findViewById(R.id.btnUpdateCustomer);

        // Récupérer l'objet Company passé par l'intent
        customer = (Customer) getIntent().getParcelableExtra("customer");

        if (customer != null) {
            etName.setText(customer.getName());
            etAddress.setText(customer.getAddress());
            etCity.setText(customer.getCity());
            etDescription.setText(customer.getDescription());
            etFirstName.setText(customer.getContact().getFirstName());
            etLastName.setText(customer.getContact().getLastName());
            etPhoneNumber.setText(customer.getContact().getPhoneNumber());

            if (customer.getType() == ECustomerType.client) {
                radioCustomer.setChecked(true);
            } else {
                radioProspect.setChecked(true);
            }
        }

        btnSave.setOnClickListener(v -> updateCustomer(customer));
    }

    private void updateCustomer(Customer customer) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());

        ECustomerType type = radioCustomer.isChecked() ? ECustomerType.client : ECustomerType.prospect;
        Contact ct = new Contact(
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                etPhoneNumber.getText().toString()
        );
        Customer updatedCustomer = new Customer(
                customer.getId(),
                etName.getText().toString(),
                etAddress.getText().toString(),
                etCity.getText().toString(),
                etDescription.getText().toString(),
                type,
                ct,
                null
        );

        communicator.updateCustomer(updatedCustomer, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(UpdateCustomerActivity.this, response.message, Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(UpdateCustomerActivity.this, error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }
}

