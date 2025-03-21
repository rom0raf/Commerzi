package com.commerzi.app.customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.commerzi.app.HomeActivity;
import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

public class CreateCustomerActivity extends AppCompatActivity {
    EditText txtName;
    EditText txtAddress;
    EditText txtCity;
    EditText txtDescription;
    EditText txtPhoneNumber;
    EditText txtFirstname;
    EditText txtLastname;
    RadioGroup radioGroupCustomerType;
    Button btnCreateNewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_customer);

        txtName = findViewById(R.id.nameField);
        txtAddress = findViewById(R.id.addressField);
        txtCity = findViewById(R.id.cityField);
        txtDescription = findViewById(R.id.descriptionField);
        txtPhoneNumber = findViewById(R.id.phoneNumberField);
        txtFirstname = findViewById(R.id.firstnameField);
        txtLastname = findViewById(R.id.lastnameField);
        radioGroupCustomerType = findViewById(R.id.radioGroupCustomerType); // Initialisation du RadioGroup
        btnCreateNewClient = findViewById(R.id.btnCreateNewCustomer);

        btnCreateNewClient.setOnClickListener(this::handleCreateClientButtonClicked);
    }

    /**
     * Handles the click event for the create client button.
     * Validates the input fields and creates a new customer if valid.
     *
     * @param view The view that was clicked.
     */
    public void handleCreateClientButtonClicked(View view) {
        String name = txtName.getText().toString();
        String address = txtAddress.getText().toString();
        String city = txtCity.getText().toString();
        String description = txtDescription.getText().toString();
        String phoneNumber = txtPhoneNumber.getText().toString();
        String firstname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();

        // Vérification de sélection d'un RadioButton
        int selectedRadioId = radioGroupCustomerType.getCheckedRadioButtonId();
        if (selectedRadioId == -1) {
            Toast.makeText(this, R.string.select_type_error, Toast.LENGTH_LONG).show();
            return;
        }

        // Récupération de la valeur sélectionnée
        RadioButton selectedRadioButton = findViewById(selectedRadioId);
        String clientType = selectedRadioButton.getText().toString().toLowerCase(); // "client" ou "prospect"

        if (name.isEmpty() || address.isEmpty() || city.isEmpty()
                || description.isEmpty() || phoneNumber.isEmpty()
                || firstname.isEmpty() || lastname.isEmpty()) {
            Toast.makeText(this, R.string.empty_field_error, Toast.LENGTH_LONG).show();
            return;
        }
        Contact contact = new Contact(firstname, lastname, phoneNumber);

        Customer customer = new Customer(null, name, address, city, description, ECustomerType.valueOf(clientType), contact, null);
        createClient(customer);
    }

    /**
     * Creates a new customer using the Communicator.
     *
     * @param customer The customer to create.
     */
    private void createClient(Customer customer) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.createCustomer(customer, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(CreateCustomerActivity.this, response.message, Toast.LENGTH_LONG).show();
                    Intent intention = new Intent(CreateCustomerActivity.this, HomeActivity.class);
                    startActivity(intention);
                },
                error -> {
                    Toast.makeText(CreateCustomerActivity.this, error.message, Toast.LENGTH_LONG).show();
                }
        ));
    }
}