package com.commerzi.app.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

/**
 * Activity for handling user signup.
 */
public class SignupActivity extends AppCompatActivity {

    private final String API_URL = "http://57.128.220.88:8080/api/user/";
    Button btnLogin;
    Button btnSignup;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtPwdConfirmation;
    EditText txtFirstname;
    EditText txtLastname;
    EditText txtAddress;
    EditText txtCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        txtEmail = findViewById(R.id.emailSignUpField);
        txtFirstname = findViewById(R.id.firstnameSignUpField);
        txtLastname = findViewById(R.id.lastnameSignUpField);
        txtPassword = findViewById(R.id.passwordSignUpField);
        txtPwdConfirmation = findViewById(R.id.confirmationSignUpField);
        txtAddress = findViewById(R.id.addressSignUpField);
        txtCity = findViewById(R.id.citySignUpField);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::onLoginButtonClicked);

        btnSignup = findViewById(R.id.btnValidateSignUp);
        btnSignup.setOnClickListener(this::handleSignupButtonClicked);
    }

    /**
     * Handles the signup button click event.
     *
     * @param view The view that was clicked.
     */
    public void handleSignupButtonClicked(View view) {
        String email = txtEmail.getText().toString();
        String firstname = txtFirstname.getText().toString();
        String lastname = txtLastname.getText().toString();
        String address = txtAddress.getText().toString();
        String password = txtPassword.getText().toString();
        String city = txtCity.getText().toString();
        String pwdConfirmation = txtPwdConfirmation.getText().toString();

        if (email.isEmpty() || password.isEmpty()
                || firstname.isEmpty() || lastname.isEmpty()
                || address.isEmpty() || pwdConfirmation.isEmpty()) {
            Toast.makeText(this, R.string.empty_field_error, Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(pwdConfirmation))  {
            Toast.makeText(this, R.string.password_error, Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User(firstname, lastname, address, city, email, password);
        signupUser(user);
    }

    /**
     * Signs up the user with the provided user data.
     *
     * @param user The user data to sign up.
     */
    private void signupUser(User user) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.signup(user, new CommunicatorCallback<>(
                response -> {
                    Toast.makeText(SignupActivity.this, response.message, Toast.LENGTH_LONG).show();
                    Intent intention = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intention);
                },
                error -> {
                    Toast.makeText(SignupActivity.this, error.message, Toast.LENGTH_LONG).show();
                }
        ));
    }

    /**
     * Handles the login button click event.
     *
     * @param view The view that was clicked.
     */
    public void onLoginButtonClicked(View view) {
        Intent intention = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intention);
    }
}