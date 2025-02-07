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
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

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

        btnSave.setOnClickListener(v -> updateClient(client));
    }

    private void updateClient(Client client) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());

        communicator.updateClient(client, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(EditClientActivity.this, response.message, Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(EditClientActivity.this, error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }
}

