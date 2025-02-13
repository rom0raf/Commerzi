package com.commerzi.app.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.commerzi.app.R;
import com.commerzi.app.auth.Session;
import com.commerzi.app.auth.User;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etAddress;
    EditText etCity;
    EditText etPassword;
    EditText etPasswordConfirmation;
    Button btnUpdateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        etFirstName = findViewById(R.id.lastnameEditField);
        etLastName = findViewById(R.id.firstnameEditField);
        etEmail = findViewById(R.id.emailEditField);
        etAddress = findViewById(R.id.addressEditField);
        etCity = findViewById(R.id.cityEditField);
        etPassword = findViewById(R.id.passwordEditField);
        etPasswordConfirmation = findViewById(R.id.confirmationEditField);
        btnUpdateProfile = findViewById(R.id.btnValidateEdit);

        btnUpdateProfile.setOnClickListener(this::onValidateButtonClicked);

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

        updateLocalProfile(user);
        updateRemoteProfile(user);
    }

    private void updateLocalProfile(User user) {
        Session.setUser(user);
    }

    private void updateRemoteProfile(User user) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.updateProfile(user, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(UpdateProfileActivity.this, response.message, Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(UpdateProfileActivity.this, error.message, Toast.LENGTH_SHORT).show();
                }
        ));
    }
}
