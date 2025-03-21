package com.commerzi.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.commerzi.app.HomeActivity;
import com.commerzi.app.R;
import com.commerzi.app.communication.Communicator;
import com.commerzi.app.communication.responses.CommunicatorCallback;

/**
 * Activity for handling user login.
 */
public class LoginActivity extends AppCompatActivity {
    EditText txtEmail;
    EditText txtPassword;
    Button btnValidate;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtEmail = findViewById(R.id.emailLoginField);
        txtPassword = findViewById(R.id.passwordLoginField);
        btnValidate = findViewById(R.id.btnValidateLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnValidate.setOnClickListener(this::handleLoginButtonClicked);
        btnSignup.setOnClickListener(this::handleSignupButtonClicked);
    }

    /**
     * Handles the login button click event.
     *
     * @param view The view that was clicked.
     */
    public void handleLoginButtonClicked(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.email_password_empty, Toast.LENGTH_LONG).show();
            return;
        }

        loginUser(email, password);
    }

    /**
     * Logs in the user with the provided email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     */
    private void loginUser(String email, String password) {
        Communicator communicator = Communicator.getInstance(getApplicationContext());
        communicator.login(email, password, new CommunicatorCallback<>(
                response ->  {
                    Session.setUser(response.user);
                    Intent intention = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intention);
                },
                error -> {
                    Toast.makeText(LoginActivity.this, error.message, Toast.LENGTH_LONG).show();
                }
        ));
    }

    /**
     * Handles the signup button click event.
     *
     * @param view The view that was clicked.
     */
    public void handleSignupButtonClicked(View view) {
        Intent intention = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intention);
    }
}