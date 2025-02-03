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

        createClient(name, address, city, description, phoneNumber, firstname, lastname, clientType);
    }

    private void createClient(String name, String address, String city,
                              String description, String phoneNumber,
                              String firstname, String lastname, String type) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("name", name);
            requestBody.put("address", address);
            requestBody.put("city", city);
            requestBody.put("description", description);

            JSONObject contact = new JSONObject();
            contact.put("phoneNumber", phoneNumber);
            contact.put("firstName", firstname);
            contact.put("lastName", lastname);
            requestBody.put("contact", contact);

            // Ajout du type (client ou prospect) à la requête
            requestBody.put("type", type.toLowerCase());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(AddClientActivity.this, R.string.client_creation_success, Toast.LENGTH_LONG).show();
                            Intent intention = new Intent(AddClientActivity.this, HomeActivity.class);
                            startActivity(intention);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null && error.networkResponse != null) {
                                String response = new String(error.networkResponse.data);
                                Toast.makeText(AddClientActivity.this, response, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddClientActivity.this, R.string.unexpected_error, Toast.LENGTH_LONG).show();
                            }
                        }
                    }) {

                @Override
                public byte[] getBody() {
                    return requestBody.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Commerzi-Auth", Session.getInstance().getApiKey());
                    return headers;
                }
            };

            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.request_creation_error, Toast.LENGTH_LONG).show();
        }
    }
}
