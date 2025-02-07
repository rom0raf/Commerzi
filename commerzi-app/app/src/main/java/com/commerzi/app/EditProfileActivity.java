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
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

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

        User user = new User(firstname, lastname, address, city, email, password);

        editLocalProfile(user);
        editRemoteProfile(user);
    }

    private void editLocalProfile(User user) {
        Session.setUser(user);
    }

    private void editRemoteProfile(User user) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.updateProfile(user, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(EditProfileActivity.this, response.message, Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(EditProfileActivity.this, error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }
}
