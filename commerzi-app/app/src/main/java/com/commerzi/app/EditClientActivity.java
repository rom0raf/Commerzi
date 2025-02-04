package com.commerzi.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditClientActivity extends AppCompatActivity {

    private EditText etName, etAddress, etCity, etDescription, etFirstName, etLastName, etPhoneNumber;
    private Button btnSave;
    private Client client;
    private RadioButton radioClient, radioProspect;
    private RadioGroup rgClientType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);

        etName = findViewById(R.id.nameEditField);
        etAddress = findViewById(R.id.addressEditField);
        etCity = findViewById(R.id.cityEditField);
        etDescription = findViewById(R.id.descriptionEditField);
        etFirstName = findViewById(R.id.firstnameEditField);
        etLastName = findViewById(R.id.lastnameEditField);
        etPhoneNumber = findViewById(R.id.phoneNumberEditField);
        rgClientType = findViewById(R.id.radioGroupEditClientType);
        radioClient = findViewById(R.id.radioEditClient);
        radioProspect = findViewById(R.id.radioEditProspect);
        btnSave = findViewById(R.id.btnUpdateClient);

        // Récupérer l'objet Company passé par l'intent
        client = (Client) getIntent().getSerializableExtra("client");

        if (client != null) {
            etName.setText(client.getName());
            etAddress.setText(client.getAddress());
            etCity.setText(client.getCity());
            etDescription.setText(client.getDescription());
            etFirstName.setText(client.getContactFirstName());
            etLastName.setText(client.getContactLastName());
            etPhoneNumber.setText(client.getContactPhoneNumber());

            if (client.getType().equalsIgnoreCase("client")) {
                radioClient.setChecked(true);
            } else {
                radioProspect.setChecked(true);
            }
        }

        btnSave.setOnClickListener(v -> updateClient());
    }

    private void updateClient() {
        String url = "http://57.128.220.88:8080/api/customers/" + client.getId();
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiKey = Session.getInstance().getApiKey();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", etName.getText().toString());
            jsonBody.put("address", etAddress.getText().toString());
            jsonBody.put("city", etCity.getText().toString());
            jsonBody.put("description", etDescription.getText().toString());
            jsonBody.put("type", radioClient.isChecked() ? "client" : "prospect");

            JSONObject contact = new JSONObject();
            contact.put("firstName", etFirstName.getText().toString());
            contact.put("lastName", etLastName.getText().toString());
            contact.put("phoneNumber", etPhoneNumber.getText().toString());

            jsonBody.put("contact", contact);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonBody,
                response -> {
                    Toast.makeText(EditClientActivity.this, "Client mis à jour", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(EditClientActivity.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Commerzi-Auth", apiKey);
                return headers;
            }
        };

        queue.add(request);
    }
}

