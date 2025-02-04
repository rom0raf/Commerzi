package com.commerzi.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etAddress;
    EditText etCity;
    EditText etPassword;
    EditText etPasswordConfirmation;
    Button btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFirstName = findViewById(R.id.lastnameEditField);
        etLastName = findViewById(R.id.firstnameEditField);
        etEmail = findViewById(R.id.emailEditField);
        etAddress = findViewById(R.id.addressEditField);
        etCity = findViewById(R.id.cityEditField);
        etPassword = findViewById(R.id.passwordEditField);
        etPasswordConfirmation = findViewById(R.id.confirmationEditField);
        btnEditProfile = findViewById(R.id.btnValidateEdit);

        btnEditProfile.setOnClickListener(this::onValidateButtonClicked);

        etFirstName.setText(Session.getUser().getFirstName());
        etLastName.setText(Session.getUser().getLastName());
        etEmail.setText(Session.getUser().getEmail());
        etAddress.setText(Session.getUser().getAddress());
        etCity.setText(Session.getUser().getCity());
    }

    private void onValidateButtonClicked(View view) {
        String firstname = etFirstName.getText().toString();
        String lastname = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String address = etAddress.getText().toString();
        String city = etCity.getText().toString();
        String password = etPassword.getText().toString();
        if(password.isEmpty()) {
            password = Session.getUser().getPassword();
        }

        editLocalProfile(firstname, lastname, email, address, city, password);
        editRemoteProfile(firstname, lastname, email, address, city, password);
    }

    private void editLocalProfile(String firstname, String lastname, String email, String address, String city, String password) {
        User user = Session.getUser();

        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setAddress(address);
        user.setCity(city);
        user.setPassword(password);
    }

    private void editRemoteProfile(String firstname, String lastname, String email, String address, String city, String password) {
        String url = "http://57.128.220.88:8080/api/user/";
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiKey = Session.getInstance().getApiKey();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstName", firstname);
            jsonBody.put("lastName", lastname);
            jsonBody.put("email", email);
            jsonBody.put("address", address);
            jsonBody.put("city", city);
            jsonBody.put("password", password);
            jsonBody.put("session", apiKey);

        } catch (Exception e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                response -> {
                    Toast.makeText(EditProfileActivity.this, "Profil mis à jour", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(EditProfileActivity.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

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
