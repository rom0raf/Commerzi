package com.commerzi.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddClientActivity extends AppCompatActivity {

    private final String API_URL = "http://57.128.220.88:8080/api/customers/";
    EditText txtName;
    EditText txtAddress;
    EditText txtCity;
    EditText txtDescription;
    EditText txtPhoneNumber;
    EditText txtFirstname;
    EditText txtLastname;
    RadioGroup radioGroupClientType; // Ajout du RadioGroup
    Button btnCreateNewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client);

        txtName = findViewById(R.id.nameField);
        txtAddress = findViewById(R.id.addressField);
        txtCity = findViewById(R.id.cityField);
        txtDescription = findViewById(R.id.descriptionField);
        txtPhoneNumber = findViewById(R.id.phoneNumberField);
        txtFirstname = findViewById(R.id.firstnameField);
        txtLastname = findViewById(R.id.lastnameField);
        radioGroupClientType = findViewById(R.id.radioGroupClientType); // Initialisation du RadioGroup
        btnCreateNewClient = findViewById(R.id.btnCreateNewClient);

        btnCreateNewClient.setOnClickListener(this::handleCreateClientButtonClicked);
    }

    public void handleCreateClientButtonClicked(View view) {
        String name = txtName.getText().toString();
        String address = txtAddress.getText().toString();
        String city = txtCity.getText().toString();
        String description = txtDescription.getText().toString();
        String phoneNumber = txtPhoneNumber.getText().toString();
        String firstname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();

        // Vérification de sélection d'un RadioButton
        int selectedRadioId = radioGroupClientType.getCheckedRadioButtonId();
        if (selectedRadioId == -1) {
            Toast.makeText(this, R.string.error_select_type, Toast.LENGTH_LONG).show();
            return;
        }

        // Récupération de la valeur sélectionnée
        RadioButton selectedRadioButton = findViewById(selectedRadioId);
        String clientType = selectedRadioButton.getText().toString().toLowerCase(); // "client" ou "prospect"

        if (name.isEmpty() || address.isEmpty() || city.isEmpty()
                || description.isEmpty() || phoneNumber.isEmpty()
                || firstname.isEmpty() || lastname.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_field, Toast.LENGTH_LONG).show();
            return;
        }
        
        Client client = new Client(null, name, address, city, description, clientType, firstname, lastname, phoneNumber);
        createClient(client);
    }

    private void createClient(Client client) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.createClient(client, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(AddClientActivity.this, response.message, Toast.LENGTH_LONG).show();
                    Intent intention = new Intent(AddClientActivity.this, HomeActivity.class);
                    startActivity(intention);
                },
                error -> {
                    Toast.makeText(AddClientActivity.this, error.message, Toast.LENGTH_LONG).show();
                }
        ));
    }
}
