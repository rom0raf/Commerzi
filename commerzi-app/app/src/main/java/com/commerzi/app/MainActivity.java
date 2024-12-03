package com.commerzi.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String STUB_EMAIL = "user@mail.com";
    private final String STUB_PASSWORD = "password";
    private final String API_URL="";
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

    public void handleLoginButtonClicked(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        Boolean areCredentialsCorrect = email.equals(STUB_EMAIL) && password.equals(STUB_PASSWORD);

        if (areCredentialsCorrect) {
            Intent intention = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intention);
        } else if (!email.equals(STUB_EMAIL)){
            Toast.makeText(this, R.string.login_email_error_message, Toast.LENGTH_LONG)
                    .show();
        } else if (!password.equals(STUB_PASSWORD)) {
            Toast.makeText(this, R.string.login_password_error_message, Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void handleSignupButtonClicked(View view) {
        Intent intention = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intention);
    }
}